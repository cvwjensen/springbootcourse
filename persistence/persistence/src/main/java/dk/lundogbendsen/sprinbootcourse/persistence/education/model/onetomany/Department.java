package dk.lundogbendsen.sprinbootcourse.persistence.education.model.onetomany;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Department {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany
    List<Employee> employees;
}
