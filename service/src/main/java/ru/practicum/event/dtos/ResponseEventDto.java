package ru.practicum.event.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dtos.location.LocationDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEventDto {

    private Long id;
    private String title;
    private LocationDto location;
}
