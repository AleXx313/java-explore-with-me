package ru.practicum.request.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.util.constant.Constants;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRequestDto {

    private Long id;
    private Long requester;
    private Long event;
    private RequestStatus status;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime created;
}
