package dk.lundogbendsen.sprinbootcourse.persistence.gamer;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
