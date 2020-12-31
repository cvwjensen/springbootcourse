package dk.lundogbendsen.sprinbootcourse.persistence.education.repository;

import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
