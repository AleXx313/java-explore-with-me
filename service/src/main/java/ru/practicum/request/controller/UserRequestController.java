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
@RequestMapping(path = "/users")
public class UserRequestController {

    private final RequestService requestService;
    private static final String REQUESTER_PATH = "/{userId}/requests";
    private static final String OWNER_PATH = "/{userId}/events/{eventId}/requests";

    @GetMapping(path = REQUESTER_PATH)
    public ResponseEntity<List<ParticipantRequestDto>> getByRequester(@PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<>(requestService.getByRequester(userId), HttpStatus.OK);
    }

    @PostMapping(path = REQUESTER_PATH)
    public ResponseEntity<ParticipantRequestDto> save(@PathVariable(value = "userId") Long userId,
                                                      @RequestParam(value = "eventId") Long eventId) {
        return new ResponseEntity<>(requestService.save(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping(path = REQUESTER_PATH + "/{requestId}/cancel")
    public ResponseEntity<ParticipantRequestDto> cancel(@PathVariable(value = "userId") Long userId,
                                                        @PathVariable(value = "requestId") Long requestId) {
        return new ResponseEntity<>(requestService.cancel(userId, requestId), HttpStatus.OK);
    }

    @GetMapping(path = OWNER_PATH)
    public ResponseEntity<List<ParticipantRequestDto>> getByOwner(@PathVariable(value = "userId") Long userId,
                                                                  @PathVariable(value = "eventId") Long eventId) {
        return new ResponseEntity<>(requestService.getByOwner(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping(path = OWNER_PATH)
    public ResponseEntity<RequestersStatusUpdateResponseDto> updateRequest(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "eventId") Long eventId,
            @Valid @RequestBody RequestersStatusUpdateDto dto) {
        return new ResponseEntity<>(requestService.updateRequest(userId, eventId, dto), HttpStatus.OK);
    }
}
