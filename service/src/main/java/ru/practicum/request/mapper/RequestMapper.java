package ru.practicum.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.request.dtos.ParticipantRequestDto;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static ParticipantRequestDto requestToDto(Request request) {
        return ParticipantRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipantRequestDto> listToDto(List<Request> list) {
        return list.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }
}
