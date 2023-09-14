package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dtos.EventRequestDto;
import ru.practicum.event.dtos.BaseEventResponseDto;
import ru.practicum.event.mapper.location.LocationMapper;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event newDtoToEvent(EventRequestDto dto){
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .location(LocationMapper.dtoToLocation(dto.getLocation()))
                .eventDate(dto.getEventDate())
                .paid(dto.getPaid())
                .requestModeration(dto.getRequestModeration())
                .participantLimit(dto.getParticipantLimit())
                .build();
    }
    public static BaseEventResponseDto eventToDto(Event event){
        return BaseEventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreated())
                .eventDate(event.getEventDate())
                .publishedOn(event.getPublishedOn())
                .initiator(UserMapper.userToShortDto(event.getInitiator()))
                .location(LocationMapper.locationToDto(event.getLocation()))
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .paid(event.getPaid())
                .requestModeration(event.getRequestModeration())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .build();
    }


}
