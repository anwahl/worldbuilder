package com.wahlhalla.worldbuilder.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wahlhalla.worldbuilder.util.exceptions.EntityIdMismatchException;
import com.wahlhalla.worldbuilder.util.exceptions.EntityNotFoundException;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> exception(EntityNotFoundException exception) {
       return new ResponseEntity<>("{\"message\": \"Object not found.\"}", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityIdMismatchException.class)
    public ResponseEntity<Object> exception(EntityIdMismatchException exception) {
       return new ResponseEntity<>("{\"message\": \"IDs do not match.\"}", HttpStatus.CONFLICT);
    }
}
