package dk.lundogbendsen.urlshortener.api;

import dk.lundogbendsen.urlshortener.service.exceptions.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerAdvicer {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAccessDenied(AccessDeniedException accessDeniedException) {

    }
}
