package dk.lundogbendsen.restassignmentapi.api;

import dk.lundogbendsen.springbootcourse.api.personservice.model.Person;
import dk.lundogbendsen.springbootcourse.api.personservice.service.PersonService;
import dk.lundogbendsen.springbootcourse.api.personservice.service.exceptions.PersonCreateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping
    public List<Person> list() {
        return personService.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person create(@RequestBody Person person) {
        final Person created = personService.create(person);
        return created;
    }

    @PostMapping("create2")
    @ResponseStatus(HttpStatus.CREATED)
    public Person create2(@RequestBody Person person) {
        final Person created;
        try {
            created = personService.create(person);
        } catch (PersonCreateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found", e);
        }
        return created;
    }

    @GetMapping("/{id}")
    public Person get(@PathVariable Long id) {
        return personService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestParam String name, @RequestParam Long age) {
        final Person person = personService.get(id);
        person.setName(name);
        person.setAge(age);
        personService.update(person);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        personService.delete(id);
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String personCreateExceptionHandler(PersonCreateException e) {
//        return e.getMessage();
//    }

}
