package ru.d1soul.departments.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadFormException extends RuntimeException {
    public BadFormException(String message) {
        super(message);
    }
}
