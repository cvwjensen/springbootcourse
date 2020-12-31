package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QBExample {
    @Id
    @GeneratedValue
    private Long id;
    private String p1;
    private Long p2;
    @ElementCollection
    private List<String> p3;
}
