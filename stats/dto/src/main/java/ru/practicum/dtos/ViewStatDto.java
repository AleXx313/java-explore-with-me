package ru.practicum.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatDto {
    private String app;
    private String uri;
    private long hits;

}
