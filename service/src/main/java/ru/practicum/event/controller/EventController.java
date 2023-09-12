package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dtos.SimpleEventDto;
import ru.practicum.event.service.EventService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/event")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<SimpleEventDto> save(@RequestBody SimpleEventDto simpleEventDto){
        return new ResponseEntity<>(eventService.save(simpleEventDto), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<SimpleEventDto> update(@PathVariable(value = "id") Long id,
                                                 @RequestBody SimpleEventDto simpleEventDto){
        return new ResponseEntity<>(eventService.update(id, simpleEventDto), HttpStatus.OK);
    }
}
