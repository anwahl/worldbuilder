package com.wahlhalla.worldbuilder.util.errors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {
    
    @GetMapping(value="/error")
    public String handleGetError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "{\"error\": \"The object or page you were looking for was not found on this resource.\"}";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "{\"error\": \"There was in internal server error. Contact a server administrator if the problem persists.\"}";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "{\"error\": \"You do not have sufficient privileges to access this resource.\"}";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "{\"error\": \"Object not found.\"}";
            } else if (statusCode == HttpStatus.CONFLICT.value()) {
                return "{\"error\": \"IDs do not match.\"}";
            } else {
                return "{\"error\": \"" + statusCode + " Error. \"}";
            }
        }
        return "Eh?";
    }

    @PostMapping(value="/error")
    public String handlePostError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "{\"error\": \"The object or page you were looking for was not found on this resource.\"}";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "{\"error\": \"There was in internal server error. Contact a server administrator if the problem persists.\"}";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "{\"error\": \"You do not have sufficient privileges to access this resource.\"}";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "{\"error\": \"Object not found.\"}";
            } else if (statusCode == HttpStatus.CONFLICT.value()) {
                return "{\"error\": \"IDs do not match.\"}";
            } else {
                return "{\"error\": \"" + statusCode + " Error. \"}";
            }
        }
        return "Eh?";
    }

    @GetMapping(value="/genericError")
    public String error() {
        return "Generic Error?";
    }

    @GetMapping(value="/catastrophicError")
    public String catastrophicError() {
        return "Catastrophic Error!";
    }
}
