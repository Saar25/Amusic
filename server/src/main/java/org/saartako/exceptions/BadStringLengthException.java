package org.saartako.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad string length")
public class BadStringLengthException extends RuntimeException {
    public BadStringLengthException() {
    }

    public BadStringLengthException(String message) {
        super(message);
    }

    public BadStringLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadStringLengthException(Throwable cause) {
        super(cause);
    }

    public BadStringLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
