package ru.practicum.compilation.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class NewCompilationRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private Set<Long> events;
    private boolean pinned;
}
