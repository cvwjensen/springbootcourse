package dk.lundogbendsen.restassignmentapi.api;

import dk.lundogbendsen.springbootcourse.api.personservice.model.Person;
import dk.lundogbendsen.springbootcourse.api.personservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Person create(@RequestParam String name, @RequestParam Long age) {
        final Person person = Person.builder().name(name).age(age).build();
        final Person created = personService.create(person);
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

}
