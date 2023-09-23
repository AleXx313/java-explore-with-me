package ru.practicum.exception;

public class ApplicationRulesViolationException extends RuntimeException {

    public ApplicationRulesViolationException(String message) {
        super(message);
    }
}
