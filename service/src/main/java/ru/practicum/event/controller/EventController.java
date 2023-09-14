package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dtos.EventRequestDto;
import ru.practicum.event.dtos.BaseEventResponseDto;
import ru.practicum.event.dtos.EventUpdateRequestDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;


    //Создание события. Возможно только пользователем.
    @PostMapping(path = "/users/{userId}/events")
    public ResponseEntity<BaseEventResponseDto> save(@PathVariable(value = "userId") Long userId,
                                                     @Valid @RequestBody EventRequestDto eventRequestDto) {
        return new ResponseEntity<>(eventService.save(userId, eventRequestDto), HttpStatus.CREATED);
    }

    //Обновление события. Возможно только пользователем или админом.
    //Пользователь
    @PatchMapping(path = "/users/{userId}/events/{eventId}")
    public ResponseEntity<BaseEventResponseDto> update(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "eventId") Long eventId,
                                                       @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return new ResponseEntity<>(eventService.updateByUser(userId, eventId, eventUpdateRequestDto), HttpStatus.OK);
    }

    //Админ
    @PatchMapping(path = "/admin/events/{eventId}")
    public ResponseEntity<BaseEventResponseDto> update(@PathVariable(value = "eventId") Long eventId,
                                                       @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return new ResponseEntity<>(eventService.updateByAdmin(eventId, eventUpdateRequestDto), HttpStatus.OK);
    }

    //Запрос событий.
    //Пользователь
    //Все события пользователя с запросами и просмотрами. Пагинация!

    //Полный обзор события по id. Запросы и просмотры также внутри.

    //Админ
    //Поиск с фильтрами. Пагинация.
    /*
    - Список пользователей
    - Список состояний
    - Список категорий
    - Временной промежуток
     */

    //Публично. Только опубликованные события. Запрос сохраняется в статистике.
    //Общий поиск с фильтрами. Пагинация. Сокращенный ответ с просмотрами
    /*
    - Текст в аннотации и в описании
    - Список категорий
    - Оплачивается ли
    - Временной промежуток
    - По доступности
    - Варианты сортировки
     */

    //Полный обзор события по id. Запросы и просмотры также внутри.
}
