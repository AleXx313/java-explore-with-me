package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dtos.EventBaseResponseDto;
import ru.practicum.event.dtos.EventFullResponseDto;
import ru.practicum.event.dtos.EventUpdateRequestDto;
import ru.practicum.event.service.EventService;
import ru.practicum.util.constant.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<EventBaseResponseDto> update(@PathVariable(value = "eventId") Long eventId,
                                                       @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return new ResponseEntity<>(eventService.updateByAdmin(eventId, eventUpdateRequestDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventFullResponseDto>> getAllByAdmin(
            @RequestParam(required = false, defaultValue = "") List<Long> users,
            @RequestParam(required = false, defaultValue = "") List<String> states,
            @RequestParam(required = false, defaultValue = "") List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        return new ResponseEntity<>(
                eventService.getAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size),
                HttpStatus.OK);
    }
}
