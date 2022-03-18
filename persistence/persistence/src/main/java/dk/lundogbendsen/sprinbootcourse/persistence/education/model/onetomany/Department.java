package dk.lundogbendsen.sprinbootcourse.persistence.education.model.onetomany;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
