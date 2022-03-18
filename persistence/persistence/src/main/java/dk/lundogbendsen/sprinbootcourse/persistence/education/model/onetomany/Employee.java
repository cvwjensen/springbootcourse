package dk.lundogbendsen.sprinbootcourse.persistence.education.model.onetomany;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
public class Employee {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    Department department;
}
