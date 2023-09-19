package ru.practicum.compilation.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.compilation.dtos.CompilationResponseDto;
import ru.practicum.compilation.dtos.NewCompilationRequest;
import ru.practicum.compilation.model.Compilation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation dtoToCompilation(NewCompilationRequest dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .build();
    }

    public static CompilationResponseDto compilationToDto(Compilation compilation) {
        return CompilationResponseDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .build();
    }

}
