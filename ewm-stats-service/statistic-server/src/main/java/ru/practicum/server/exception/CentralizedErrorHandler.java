package ru.practicum.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CentralizedErrorHandler {
    @ExceptionHandler(ValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleUserValidException(final ValidException e) {
        return new ResponseEntity<>(
                new ResponseBody(e.getMessage()), HttpStatus.valueOf(400));
    }
}