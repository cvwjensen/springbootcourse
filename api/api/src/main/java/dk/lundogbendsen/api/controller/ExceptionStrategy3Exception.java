package dk.lundogbendsen.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExceptionStrategy3Exception extends ResponseStatusException {
    public ExceptionStrategy3Exception(HttpStatus status) {
        super(status);
    }
}
