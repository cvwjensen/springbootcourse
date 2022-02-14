package dk.lundogbendsen.springbootcourse.testing.service;

import dk.lundogbendsen.springbootcourse.testing.model.Person;
import dk.lundogbendsen.springbootcourse.testing.repository.PersonRepository;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


/**
 * Sliced SpringBoot application with only MyService and PersonRepository as Beans
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={MyService.class})
public class MyServiceSlicedExtension {
    @MockBean
    private PersonRepository personRepository;

    @Autowired
    MyService myService;

    @BeforeEach
    public void init() {
        Person person = new Person();
        person.setName("Christian");
        person.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
    }

    @Test
    public void testGetPerson() {
        Person person = myService.get(1L);
        assertEquals(person.getName(), "Christian");
    }

}
