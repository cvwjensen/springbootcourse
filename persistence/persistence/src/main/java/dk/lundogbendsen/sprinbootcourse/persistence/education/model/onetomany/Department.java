package dk.lundogbendsen.sprinbootcourse.persistence.education.model.onetomany;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany
    @BatchSize(size = 1)
    List<Employee> employees;
}
