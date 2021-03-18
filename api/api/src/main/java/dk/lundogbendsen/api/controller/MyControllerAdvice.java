package dk.lundogbendsen.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public String exceptionHandlerStrategy1(ExceptionStrategy1Exception e) {
        return "strategy1 from ADVICE HANDLER";
    }

    @ExceptionHandler
    public String exceptionStrategy2(ExceptionStrategy2Exception e) {
        return "ExceptionStrategy2: " + e.getMessage();
    }

//    @ExceptionHandler
//    public String runtimeExceptions(RuntimeException e) {
//        return "There was a error: " + e.getMessage();
//    }
}
