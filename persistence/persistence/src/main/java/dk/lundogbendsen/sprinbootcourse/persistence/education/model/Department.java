package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Department {
    @Id @GeneratedValue
    private Long id;
    @OneToMany
    List<Employee> employees;
}
