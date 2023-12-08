package ru.practicum.mainservice.exception;

public class ValidException extends RuntimeException {
    public ValidException(String message) {
        super(message);
    }

    public ValidException(String message, Object... args) {
        super(String.format(message, args));
    }
}
