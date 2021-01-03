package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Soldier {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    Rifle rifle;
}
