package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.event.dtos.SimpleEventDto;
import ru.practicum.event.mapper.location.LocationMapper;
import ru.practicum.event.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event dtoToEvent(SimpleEventDto dto){
        return Event.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .location(LocationMapper.dtoToLocation(dto.getLocation()))
                .build();
    }
    public static SimpleEventDto eventToDto(Event event){
        return SimpleEventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .location(LocationMapper.locationToDto(event.getLocation()))
                .build();
    }
}
