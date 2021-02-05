package dk.lundogbendsen.urlshortener.api;

import dk.lundogbendsen.urlshortener.service.exceptions.AccessDeniedException;
import dk.lundogbendsen.urlshortener.service.exceptions.TokenAlreadyExistsException;
import dk.lundogbendsen.urlshortener.service.exceptions.UserExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvicer {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public void handleAccessDenied(AccessDeniedException accessDeniedException) {
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public void handleRuntime(RuntimeException runtimeException) {
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({TokenAlreadyExistsException.class, UserExistsException.class})
    public String handleConflict(Exception runtimeException) {
        return runtimeException.getMessage();
    }

//    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
//    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
//    public String mediaTypeNotAcceptable(HttpMediaTypeNotSupportedException runtimeException) {
//        return runtimeException.getMessage();
//    }
}
