package ru.practicum.event.mapper.location;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.event.dtos.location.LocationDto;
import ru.practicum.event.model.location.Location;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {

    public static LocationDto locationToDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location dtoToLocation(LocationDto dto) {
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}
