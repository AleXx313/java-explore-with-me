package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dtos.EventRequestDto;
import ru.practicum.event.dtos.BaseEventResponseDto;
import ru.practicum.event.dtos.EventUpdateRequestDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.location.LocationMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.UpdateState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ApplicationRulesViolationException;
import ru.practicum.exception.ModelNotFoundException;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {
    //Репозиторий сущности
    private final EventRepository eventRepository;
    //Сервисы задействованных сущностей
    private final UserService userService;
    private final CategoryService categoryService;


    @Transactional
    public BaseEventResponseDto save(long userId, EventRequestDto eventRequestDto) {
        if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ApplicationRulesViolationException(
                    "Событие может начаться не раньше чем через 2 часа после создания публикации");
        }
        Event event = EventMapper.newDtoToEvent(eventRequestDto);
        event.setInitiator(userService.findById(userId));
        event.setCategory(categoryService.findById(eventRequestDto.getCategory()));
        event.setCreated(LocalDateTime.now());
        event.setState(EventState.PENDING);
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        Event savedEvent = eventRepository.save(event);

        return EventMapper.eventToDto(savedEvent);
    }

    public BaseEventResponseDto updateByUser(long userId, long eventId, EventUpdateRequestDto dto) {
        Event event = findById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ModelNotFoundException("Событие может редактировать только его создатель!");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new ApplicationRulesViolationException("Событие уже опубликовано! Редактирование не доступно!");
        }
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ApplicationRulesViolationException(
                    "Событие должно начаться не раньше чем через 2 часа после создания заявки!");
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == UpdateState.SEND_TO_REVIEW) event.setState(EventState.PENDING);
            if (dto.getStateAction() == UpdateState.CANCEL_REVIEW) event.setState(EventState.CANCELED);
        }
        updateEvent(dto, event);
        return EventMapper.eventToDto(eventRepository.save(event));
    }


    public BaseEventResponseDto updateByAdmin(Long eventId, EventUpdateRequestDto dto) {
        Event event = findById(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new ApplicationRulesViolationException("Событие уже опубликовано! Редактирование не доступно!");
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == UpdateState.PUBLISH_EVENT && event.getState() == EventState.CANCELED) {
                throw new ApplicationRulesViolationException(
                        "Опубликовать можно только событие ожидающее публикации!");
            }
        }
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ApplicationRulesViolationException(
                    "Событие должно начаться не раньше чем через час после публикации!");
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == UpdateState.PUBLISH_EVENT) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (dto.getStateAction() == UpdateState.REJECT_EVENT) event.setState(EventState.CANCELED);
        }
        updateEvent(dto, event);
        return EventMapper.eventToDto(eventRepository.save(event));
    }

    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ModelNotFoundException("Событие не найдено!"));
    }

    private void updateEvent(EventUpdateRequestDto dto, Event event) {
        if (dto.getLocation() != null) event.setLocation(LocationMapper.dtoToLocation(dto.getLocation()));
        if (dto.getCategory() != null) event.setCategory(categoryService.findById(dto.getCategory()));
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
    }
}
