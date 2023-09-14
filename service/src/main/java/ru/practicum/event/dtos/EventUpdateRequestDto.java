package ru.practicum.event.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dtos.location.LocationDto;
import ru.practicum.event.model.UpdateState;
import ru.practicum.util.constant.Constants;

import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateRequestDto {

    @Size(min = 20, max = 2000)
    private String annotation;
    @Size(min = 3, max = 120)
    private String title;
    @Positive
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @Future
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
    private UpdateState stateAction;
}
