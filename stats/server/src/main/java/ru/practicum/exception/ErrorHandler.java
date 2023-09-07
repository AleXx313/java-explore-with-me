package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptions(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                new ErrorResponse("Передан некорректный объект!", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleNotSpecializedExceptions(final Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse("Неизвестная ошибка!", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
