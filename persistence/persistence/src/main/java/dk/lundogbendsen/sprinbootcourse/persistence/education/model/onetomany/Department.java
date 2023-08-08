package dk.lundogbendsen.sprinbootcourse.persistence.education.model.onetomany;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
