package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptions(final MethodArgumentNotValidException e) {
        log.info("Получен статус 400 BadRequest {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse("Передан некорректный объект!", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleWrongParameterException(final MissingServletRequestParameterException e) {
        log.info("Получен статус 400 BadRequest {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse("Не передан необходимый параметр запроса!", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleNotSpecializedExceptions(final Exception e) {
        log.info("Получен статус 500 InternalServerError {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse("Неизвестная ошибка!", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
