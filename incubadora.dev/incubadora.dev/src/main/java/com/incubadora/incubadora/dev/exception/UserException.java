package com.incubadora.incubadora.dev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Esta anotación le dice a Spring que devuelva un código de estado 409 Conflict
@ResponseStatus(HttpStatus.CONFLICT)
public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
