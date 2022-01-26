package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
