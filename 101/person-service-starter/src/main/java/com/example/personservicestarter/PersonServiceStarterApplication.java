package com.example.personservicestarter;

import dk.lundogbendsen.springbootcourse.api.personservice.service.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(PersonService.class)
public class PersonServiceStarterApplication {

    @Bean
    @ConditionalOnMissingBean(PersonService.class)
    public PersonService personService() {
        return new PersonService();
    }

}
