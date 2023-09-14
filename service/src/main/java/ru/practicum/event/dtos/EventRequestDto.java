package ru.practicum.event.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.event.dtos.location.LocationDto;
import ru.practicum.util.constant.Constants;
import ru.practicum.util.validation.StartAfterTwoHours;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    @Positive
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    @Future
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;

    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
}
