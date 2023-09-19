package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dtos.EndpointHitDto;
import ru.practicum.event.dtos.*;
import ru.practicum.event.service.EventService;
import ru.practicum.util.constant.Constants;
import ru.practicum.util.enums.SortType;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;
    private final StatsClient statsClient;


    //Создание события. Возможно только пользователем.
    @PostMapping(path = "/users/{userId}/events")
    public ResponseEntity<EventBaseResponseDto> save(@PathVariable(value = "userId") Long userId,
                                                     @Valid @RequestBody EventRequestDto eventRequestDto) {
        return new ResponseEntity<>(eventService.save(userId, eventRequestDto), HttpStatus.CREATED);
    }

    //Обновление события. Возможно только пользователем или админом.
    //Пользователь
    @PatchMapping(path = "/users/{userId}/events/{eventId}")
    public ResponseEntity<EventBaseResponseDto> update(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "eventId") Long eventId,
                                                       @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return new ResponseEntity<>(eventService.updateByUser(userId, eventId, eventUpdateRequestDto), HttpStatus.OK);
    }

    //Админ
    @PatchMapping(path = "/admin/events/{eventId}")
    public ResponseEntity<EventBaseResponseDto> update(@PathVariable(value = "eventId") Long eventId,
                                                       @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return new ResponseEntity<>(eventService.updateByAdmin(eventId, eventUpdateRequestDto), HttpStatus.OK);
    }

    //Запрос событий.
    //Публично. Только опубликованные события. Запрос сохраняется в статистике.
    //Общий поиск с фильтрами. Пагинация. Сокращенный ответ с просмотрами. Сохранение статистики
    /*
    - Текст в аннотации и в описании
    - Список категорий
    - Оплачивается ли
    - Временной промежуток
    - По доступности
    - Варианты сортировки
     */
    @GetMapping(path = "/events")
    public ResponseEntity<List<EventPublicResponseDto>> findAllPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false, defaultValue = "") List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortType sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
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

    //Полный обзор события по id. Запросы и просмотры также внутри. Сохранение статистики
    @GetMapping(path = "/events/{eventId}")
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

    //Админ
    //Поиск с фильтрами. Пагинация.
    /*
    - Список пользователей
    - Список состояний
    - Список категорий
    - Временной промежуток
     */
    @GetMapping(path = "/admin/events")
    public ResponseEntity<List<EventFullResponseDto>> getAllByAdmin(
            @RequestParam(required = false, defaultValue = "") List<Long> users,
            @RequestParam(required = false, defaultValue = "") List<String> states,
            @RequestParam(required = false, defaultValue = "") List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        return new ResponseEntity<>(
                eventService.getAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size),
                HttpStatus.OK);
    }

    //Пользователь
    //Все события пользователя с запросами и просмотрами. Пагинация!
    @GetMapping(path = "/users/{userId}/events")
    public ResponseEntity<List<EventPublicResponseDto>> getAllByUser(
            @PathVariable(value = "userId") Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getAllByUser(userId, from, size), HttpStatus.OK);
    }

    //Полный обзор события по id. Запросы и просмотры также внутри.
    @GetMapping(path = "/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullResponseDto> getByIdByUser(@PathVariable(value = "userId") Long userId,
                                                              @PathVariable(value = "eventId") Long eventId) {
        return new ResponseEntity<>(eventService.getByIdByUser(userId, eventId), HttpStatus.OK);
    }
}
