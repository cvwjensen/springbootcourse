package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Department {
    @Id @GeneratedValue
    private Long id;
    @OneToMany
    List<Employee> employees;
}
