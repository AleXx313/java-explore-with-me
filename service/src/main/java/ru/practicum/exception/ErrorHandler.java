package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.info("Получен статус 409 Conflict {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(
                "Нарушены ограничения базы данных!",
                e.getMessage(), HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptions(final MethodArgumentNotValidException e) {
        log.info("Получен статус 400 BadRequest {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(
                "Передан некорректный объект!",
                e.getMessage(), HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException e) {
        log.info("Получен статус 400 BadRequest {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(
                "Передан некорректный объект!",
                e.getMessage(), HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ModelNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleModelNotFoundExceptions(final ModelNotFoundException e) {
        log.info("Получен статус 400 BadRequest {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(
                "Передан некорректный объект!",
                e.getMessage(), HttpStatus.NOT_FOUND.name(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleNotSpecializedException(final Exception e) {
        log.info("Получен статус 500 InternalServerError {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(
                "Неизвестная ошибка!",
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
