package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Employee {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    Department department;
}
