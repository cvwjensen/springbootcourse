package dk.lundogbendsen.restassignmentapi.api;

import dk.lundogbendsen.springbootcourse.api.personservice.service.exceptions.PersonCreateException;
import dk.lundogbendsen.springbootcourse.api.personservice.service.exceptions.PersonNotFoundException;
import dk.lundogbendsen.springbootcourse.api.personservice.service.exceptions.PersonUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PersonApiControllerAdvice {
    @ExceptionHandler({PersonCreateException.class, PersonUpdateException.class, PersonNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception e) {
        return e.getMessage();
    }
}
