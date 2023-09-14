package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.ApplicationRulesViolationException;
import ru.practicum.exception.ModelNotFoundException;
import ru.practicum.request.dtos.ParticipantRequestDto;
import ru.practicum.request.dtos.RequestersStatusUpdateDto;
import ru.practicum.request.dtos.RequestersStatusUpdateResponseDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    private final EventService eventService;
    private final UserService userService;

    //Проверка на повторный запрос ++
    //Нельзя участвовать в своем событии 409 ++
    //Нельзя участвовать в неопубликованном событии 409 ++
    //Если достигнут лимит участников то нужно вернуть 409 ++
    //Если нет премодерации, то автоматически запрос становится Confirmed
    public ParticipantRequestDto save(Long userId, Long eventId) {
        if (isRepeat(userId, eventId)) {
            throw new ApplicationRulesViolationException("Нельзя подать повторную заявку на участие в событии!");
        }
        User user = userService.findById(userId);
        Event event = eventService.findById(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ApplicationRulesViolationException("Нельзя подать заявку на участие в своем событии!");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ApplicationRulesViolationException("Нельзя подать заявку на участие в неопубликованном событии!");
        }
        if (event.getParticipantLimit() <= getNumOfTakenSeats(eventId) && event.getParticipantLimit() != 0) {
            throw new ApplicationRulesViolationException("Мест нет!");
        }
        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return RequestMapper.requestToDto(requestRepository.save(request));
    }

    public ParticipantRequestDto cancel(Long userId, Long requestId) {
        Request request = findRequest(requestId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new ModelNotFoundException("Отказаться от участия может только пользователь создавший запрос!");
        }
        request.setStatus(RequestStatus.CANCELED);
        Request updatedRequest = requestRepository.save(request);
        return RequestMapper.requestToDto(updatedRequest);
    }

    public List<ParticipantRequestDto> getByRequester(Long userId) {
        return RequestMapper.listToDto(requestRepository.findAllByRequesterId(userId));
    }

    public List<ParticipantRequestDto> getByOwner(Long userId, Long eventId) {
        userService.findById(userId);
        Event event = eventService.findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ApplicationRulesViolationException("Только инициатор события может просматривать подробную " +
                    "информацию о запросах на участие!");
        }
        return RequestMapper.listToDto(requestRepository.findAllByEventId(eventId));
    }

    //Если лимит превышен, то 409 ++
    //Если лимит 0, то подтверждение не требуется?++
    //Статус можно менять только для пендингов ++
    //Если в процессе подтверждения, лимит исчерпается, то все не вошедшие в лимит отклоняются ++
    public RequestersStatusUpdateResponseDto updateRequest(Long userId, Long eventId, RequestersStatusUpdateDto dto) {

        userService.findById(userId);
        Event event = eventService.findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ApplicationRulesViolationException("Только инициатор события может просматривать подробную " +
                    "информацию о запросах на участие!");
        }
        List<Request> requestList = requestRepository.findAllByIdIn(dto.getRequestIds());
        RequestersStatusUpdateResponseDto dtoOut = new RequestersStatusUpdateResponseDto();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0){
            dtoOut.getConfirmedRequests().addAll(RequestMapper.listToDto(requestList));
            return dtoOut;
        }
        if (dto.getStatus() != RequestStatus.CONFIRMED && dto.getStatus() != RequestStatus.REJECTED) {
            throw new ApplicationRulesViolationException("Можно либо подтвердить запрос либо отказать в запросе!");
        }
        if (requestList.stream().anyMatch(request -> request.getStatus() != RequestStatus.PENDING)) {
            throw new ApplicationRulesViolationException("Статус одной или нескольких заявок не \"PENDING\"!");
        }
        int numOfFreeSeats = event.getParticipantLimit() - getNumOfTakenSeats(eventId);
        if (numOfFreeSeats == 0){
            throw new ApplicationRulesViolationException("Мест нет!");
        }

        if (numOfFreeSeats >= dto.getRequestIds().size() || dto.getStatus() == RequestStatus.REJECTED) {
            requestList.forEach(request -> request.setStatus(dto.getStatus()));

        } else {
            requestList.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            requestList.stream().limit(numOfFreeSeats).forEach(request -> request.setStatus(RequestStatus.CONFIRMED));
        }
        requestRepository.saveAll(requestList);
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        for (Request request : requestList){
            if(request.getStatus() == RequestStatus.CONFIRMED){

                confirmedRequests.add(request);
            } else {
                rejectedRequests.add(request);
            }
        }
        dtoOut.setConfirmedRequests(RequestMapper.listToDto(confirmedRequests));
        dtoOut.setRejectedRequests(RequestMapper.listToDto(rejectedRequests));
        return dtoOut;
    }

    //Внутреннее пользование
    public Request findRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new ModelNotFoundException(String.format("Запрос с id - %d не найден!", requestId)));
    }

    public boolean isRepeat(Long userId, Long eventId) {
        Optional<Request> request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        return request.isPresent();
    }

    public Integer getNumOfTakenSeats(Long eventId) {
        return requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }
}
