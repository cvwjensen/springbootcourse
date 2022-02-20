package dk.lundogbendsen.sprinbootcourse.persistence.education.model.onetoone;

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
    @OneToOne //(mappedBy = "rifle")
//        @JoinColumn(unique = true) // Enforce one-to-one
        Soldier soldier;
}
