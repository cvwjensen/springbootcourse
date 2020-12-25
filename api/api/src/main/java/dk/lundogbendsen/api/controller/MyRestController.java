package dk.lundogbendsen.api.controller;

import dk.lundogbendsen.api.model.Person;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class MyRestController {
    @GetMapping
    public String helloWorld(@RequestParam String message) {
        return "Hello World: " + message;
    }

    @GetMapping("{id}")
    public Long pathMapping(@PathVariable Long id) {
        return id;
    }

    @PostMapping
    public void receivePerson(@RequestBody Person person) {
        System.out.println("person = " + person);
    }

    @PostMapping("/file")
    public String receiveFile(@RequestParam("file") MultipartFile file, @RequestHeader(name = "Content-Type") String contentType) throws IOException {
        final String content = new String(file.getBytes());
        System.out.println("content = " + content);
        System.out.println("contentType = " + contentType);
        return file.getOriginalFilename();
    }

    @GetMapping("throws")
    public String throwsException() {
        throw new RuntimeException("Hello");
    }

    @GetMapping("person")
    public Person getPerson() {
        Person p = new Person();
        p.setFirstName("Christian");
        p.setLastName("Jensen");
        return p;
    }

    @GetMapping("person-special")
    public ResponseEntity<Person> getPersonSpecial() {
        final Person person = new Person();
        person.setFirstName("Christian");
        person.setLastName("Jensen");
        HttpHeaders headers = new HttpHeaders();
        headers.add("TraceId", "abc123");
        final ResponseEntity<Person> ok = new ResponseEntity<>(person, headers, HttpStatus.OK);
        return ok;
    }
}
