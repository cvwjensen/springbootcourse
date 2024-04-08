package dk.lundogbendsen.sprinbootcourse.persistence.gamer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Gamer {
    @Id
    @GeneratedValue
    private Long id;
    private String alias;
    private String avatar;
}
