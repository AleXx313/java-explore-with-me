package ru.practicum.event.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dtos.CategoryDto;
import ru.practicum.event.dtos.location.LocationDto;
import ru.practicum.event.model.EventState;
import ru.practicum.user.dtos.UserShortDto;
import ru.practicum.util.constant.Constants;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventBaseResponseDto {

    private Long id;

    private String title;
    private String annotation;
    private String description;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime createdOn;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime eventDate;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime publishedOn;

    private LocationDto location;
    private CategoryDto category;
    private UserShortDto initiator;

    private Boolean paid;
    private Boolean requestModeration;

    private Integer participantLimit;

    private EventState state;

}
