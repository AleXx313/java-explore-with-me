package ru.practicum.compilation.dtos;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.dtos.EventPublicResponseDto;

import java.util.List;

@Data
@Builder
public class CompilationResponseDto {

    private Long id;
    private String title;
    private List<EventPublicResponseDto> events;
    private boolean pinned;
}
