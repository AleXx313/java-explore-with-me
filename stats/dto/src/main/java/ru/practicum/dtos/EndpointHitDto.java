package ru.practicum.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {

    @NotNull
    @Size(max = 255)
    private String app;
    @NotNull
    @Size(max = 255)
    private String uri;
    @NotNull
    @Size(max = 16)
    private String ip;
    private LocalDateTime hitTime = LocalDateTime.now();
}
