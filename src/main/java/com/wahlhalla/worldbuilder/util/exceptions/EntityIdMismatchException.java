package com.wahlhalla.worldbuilder.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityIdMismatchException extends RuntimeException {
    
    public EntityIdMismatchException() {
        super();
    }
    public EntityIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
    public EntityIdMismatchException(String message) {
        super(message);
    }
    public EntityIdMismatchException(Throwable cause) {
        super(cause);
    }
}
