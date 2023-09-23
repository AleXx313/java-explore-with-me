package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dtos.*;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventBaseResponseDto> save(@PathVariable(value = "userId") Long userId,
                                                     @Valid @RequestBody EventRequestDto eventRequestDto) {
        return new ResponseEntity<>(eventService.save(userId, eventRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<EventBaseResponseDto> update(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "eventId") Long eventId,
                                                       @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return new ResponseEntity<>(eventService.updateByUser(userId, eventId, eventUpdateRequestDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventPublicResponseDto>> getAllByUser(
            @PathVariable(value = "userId") Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getAllByUser(userId, from, size), HttpStatus.OK);
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<EventFullResponseDto> getByIdByUser(@PathVariable(value = "userId") Long userId,
                                                              @PathVariable(value = "eventId") Long eventId) {
        return new ResponseEntity<>(eventService.getByIdByUser(userId, eventId), HttpStatus.OK);
    }
}
