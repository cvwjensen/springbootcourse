package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course extends AuditedEntity{
//    @Id
//    @GeneratedValue
//    private Long id;
    private String subject;
    private Integer points;
    @ManyToOne
    private Teacher teacher;
    @ManyToMany
    private Set<Student> students = new HashSet<>();
}
