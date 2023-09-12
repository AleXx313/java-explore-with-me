package ru.practicum.event.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dtos.location.LocationDto;
import ru.practicum.event.model.location.Location;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEventDto {

    private Long id;
    private String title;
    private LocationDto location;

}
