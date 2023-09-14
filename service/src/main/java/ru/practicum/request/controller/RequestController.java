package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dtos.ParticipantRequestDto;
import ru.practicum.request.dtos.RequestersStatusUpdateDto;
import ru.practicum.request.dtos.RequestersStatusUpdateResponseDto;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private static final String REQUESTER_PATH = "/users/{userId}/requests";
    private static final String OWNER_PATH = "/users/{userId}/events/{eventId}/requests";

    //Получение информации о запросах пользователя
    @GetMapping(path = REQUESTER_PATH)
    public ResponseEntity<List<ParticipantRequestDto>> getByRequester(@PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<>(requestService.getByRequester(userId), HttpStatus.OK);
    }

    //Добавление запроса на участие
    @PostMapping(path = REQUESTER_PATH)
    public ResponseEntity<ParticipantRequestDto> save(@PathVariable(value = "userId") Long userId,
                                                      @RequestParam(value = "eventId") Long eventId) {
        return new ResponseEntity<>(requestService.save(userId, eventId), HttpStatus.CREATED);
    }

    //Отмена запроса на участие
    @PatchMapping(path = REQUESTER_PATH + "/{requestId}/cancel")
    public ResponseEntity<ParticipantRequestDto> cancel(@PathVariable(value = "userId") Long userId,
                                                        @PathVariable(value = "requestId") Long requestId) {
        return new ResponseEntity<>(requestService.cancel(userId, requestId), HttpStatus.OK);
    }

    //Получение информации о запросах на участие в событии пользователя
    @GetMapping(path = OWNER_PATH)
    public ResponseEntity<List<ParticipantRequestDto>> getByOwner(@PathVariable(value = "userId") Long userId,
                                                                  @PathVariable(value = "eventId") Long eventId) {
        return new ResponseEntity<>(requestService.getByOwner(userId, eventId), HttpStatus.OK);
    }
    //Подтверждение и отказ в запросе
    @PatchMapping(path = OWNER_PATH)
    public ResponseEntity<RequestersStatusUpdateResponseDto> updateRequest(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "eventId") Long eventId,
            @Valid @RequestBody RequestersStatusUpdateDto dto){
        return new ResponseEntity<>(requestService.updateRequest(userId, eventId,dto), HttpStatus.OK);
    }
}
