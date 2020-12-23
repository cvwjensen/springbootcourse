package dk.lundogbendsen.api.controller;

import dk.lundogbendsen.api.model.Person;
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
    public Person receivePerson(@RequestBody Person person) {
        return person;
    }

    @PostMapping("/file")
    public String receiveFile(@RequestParam("file") MultipartFile file, @RequestHeader(name = "Content-Type") String contentType) throws IOException {
        final String content = new String(file.getBytes());
        System.out.println("content = " + content);
        System.out.println("contentType = " + contentType);
        return file.getOriginalFilename();
    }
}
