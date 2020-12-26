package dk.lundogbendsen.api.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler
    public String exceptionStrategy2(ExceptionStrategy2Exception e) {
        return "ExceptionStrategy2: " + e.getMessage();
    }
    @ExceptionHandler
    public String runtimeExceptions(RuntimeException e) {
        return "There was a error: " + e.getMessage();
    }
}
