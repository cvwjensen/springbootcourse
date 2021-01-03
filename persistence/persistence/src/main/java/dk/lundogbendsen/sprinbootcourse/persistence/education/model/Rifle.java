package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Rifle {
    @Id
    private Long id;
    @MapsId
    @OneToOne
    Soldier soldier;
}
