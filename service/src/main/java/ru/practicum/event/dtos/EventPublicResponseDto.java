package ru.practicum.event.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.category.dtos.CategoryDto;
import ru.practicum.user.dtos.UserShortDto;
import ru.practicum.util.constant.Constants;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicResponseDto {
    private Long id;

    private String title;
    private String annotation;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime eventDate;

    private CategoryDto category;
    private UserShortDto initiator;

    private Boolean paid;

    private long views;
    private Integer confirmedRequests;
}
