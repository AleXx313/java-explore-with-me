package ru.practicum.request.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestersStatusUpdateDto {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
