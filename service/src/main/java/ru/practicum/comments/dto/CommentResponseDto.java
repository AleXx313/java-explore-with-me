package ru.practicum.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comments.model.ImpressionStatus;
import ru.practicum.util.constant.Constants;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private ImpressionStatus status;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime created;
    private Long eventId;
    private Long userId;
    private long likes;
    private long dislikes;
}
