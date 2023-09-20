package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dtos.EndpointHitDto;
import ru.practicum.event.dtos.EventFullResponseDto;
import ru.practicum.event.dtos.EventPublicResponseDto;
import ru.practicum.event.service.EventService;
import ru.practicum.util.constant.Constants;
import ru.practicum.util.enums.SortType;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {

    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<List<EventPublicResponseDto>> findAllPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false, defaultValue = "") List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortType sort,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {

        statsClient.saveHit(EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .app("ewm")
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
        return new ResponseEntity<>(eventService.getAllByPublic(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size), HttpStatus.OK);
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<EventFullResponseDto> getByIdPublic(@PathVariable(value = "eventId") Long eventId,
                                                              HttpServletRequest request) {
        statsClient.saveHit(EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .app("ewm")
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
        return new ResponseEntity<>(eventService.getByIdPublic(eventId), HttpStatus.OK);
    }
}
