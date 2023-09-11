package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String reason;
    private String message;
    private String status;
    private LocalDateTime timestamp;
}
