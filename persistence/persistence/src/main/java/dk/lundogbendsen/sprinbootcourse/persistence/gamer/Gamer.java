package dk.lundogbendsen.sprinbootcourse.persistence.gamer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entity
@Getter
@Setter
public class Gamer {
    @Id
    private String id;
    private String alias;
    private String avatar;
}
