package com.wahlhalla.worldbuilder.util;

import java.net.http.HttpHeaders;
import java.util.NoSuchElementException;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wahlhalla.worldbuilder.util.exceptions.EntityIdMismatchException;
import com.wahlhalla.worldbuilder.util.exceptions.EntityNotFoundException;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler  {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> exception(EntityNotFoundException exception) {
       return new ResponseEntity<>("{\"error\": \"Object not found.\"}", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityIdMismatchException.class)
    public ResponseEntity<Object> exception(EntityIdMismatchException exception) {
       return new ResponseEntity<>("{\"error\": \"IDs do not match.\"}", HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<Object> exception(NoSuchElementException exception) {
       return new ResponseEntity<>("{\"error\": \"Object was not found.\"}", HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(value = PSQLException.class)
    public ResponseEntity<Object> exception(PSQLException exception) {
       return new ResponseEntity<>("{\"error\": \"Problem with object: \"" + exception + "}", HttpStatus.CONFLICT);
    }


   protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
      return new ResponseEntity<>("{\"error\": \"Bad Request: \"" + ex + " }", HttpStatus.BAD_REQUEST);
   }
}
