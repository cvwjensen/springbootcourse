package dk.lundogbendsen.api.controller;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

//@RestController
//@RequestMapping("/error")
public class MyErrorController implements ErrorController {
    public String getErrorPath() {
        return "/error";
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @GetMapping
    public Map<String, Object> handleError(ServletWebRequest webRequest) {
        final DefaultErrorAttributes defaultErrorAttributes = new DefaultErrorAttributes();
        final Map<String, Object> errorAttributes = defaultErrorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE));
        System.out.println("errorAttributes = " + errorAttributes);
        return Map.of("Message", errorAttributes.get("error"));
    }
}
