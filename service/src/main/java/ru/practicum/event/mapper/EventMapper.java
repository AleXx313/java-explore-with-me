package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dtos.EventFullResponseDto;
import ru.practicum.event.dtos.EventPublicResponseDto;
import ru.practicum.event.dtos.EventRequestDto;
import ru.practicum.event.dtos.EventBaseResponseDto;
import ru.practicum.event.mapper.location.LocationMapper;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event newDtoToEvent(EventRequestDto dto) {
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

    public static EventBaseResponseDto eventToDto(Event event) {
        return EventBaseResponseDto.builder()
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

    public static EventFullResponseDto eventToFullDto(Event event) {
        return EventFullResponseDto.builder()
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

    public static EventPublicResponseDto eventToPublicDto(Event event) {
        return EventPublicResponseDto.builder()
                .eventDate(event.getEventDate())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .initiator(UserMapper.userToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .id(event.getId())
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .build();
    }


}
