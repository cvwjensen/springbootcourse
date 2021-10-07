package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Rifle {
    @Id
    @GeneratedValue
    private Long id;

//    @MapsId
    @OneToOne(mappedBy = "rifle")
    Soldier soldier;
}
