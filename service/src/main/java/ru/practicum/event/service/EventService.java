package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.dtos.SimpleEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ModelNotFoundException;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public SimpleEventDto save(SimpleEventDto simpleEventDto) {
        Event event = eventRepository.save(EventMapper.dtoToEvent(simpleEventDto));
        return EventMapper.eventToDto(event);
    }

    public SimpleEventDto update(Long id, SimpleEventDto simpleEventDto) {
        eventRepository.findById(id).orElseThrow(()-> new ModelNotFoundException("Событие не найдено!"));
        simpleEventDto.setId(id);
        Event event = eventRepository.save(EventMapper.dtoToEvent(simpleEventDto));

        return EventMapper.eventToDto(event);
    }
}
