package dk.lundogbendsen.api.controller;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

//@RestController
//@RequestMapping("/error")
public class MyErrorController implements ErrorController {

    @RequestMapping
    public ResponseEntity<Map<String, Object>> handleError(ServletWebRequest webRequest) {
        final DefaultErrorAttributes defaultErrorAttributes = new DefaultErrorAttributes();
        final Map<String, Object> errorAttributes = defaultErrorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE));
        System.out.println("errorAttributes = " + errorAttributes);
        Map<String, Object> body = Map.of("Message", errorAttributes.get("error"));
        return new ResponseEntity<>(body, HttpStatus.valueOf((Integer) errorAttributes.get("status")));
    }
}
