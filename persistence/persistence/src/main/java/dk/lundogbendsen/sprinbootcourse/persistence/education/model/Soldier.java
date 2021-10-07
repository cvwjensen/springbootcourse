package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Soldier {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne //(mappedBy = "soldier")
    Rifle rifle;
}
