package dk.lundogbendsen.restassignmentapi;

import dk.lundogbendsen.springbootcourse.api.personservice.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonConfig {

    @Bean
    public PersonService personService() {
        return new PersonService();
    }
}
