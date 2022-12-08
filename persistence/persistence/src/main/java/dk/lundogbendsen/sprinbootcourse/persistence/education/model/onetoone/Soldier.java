package dk.lundogbendsen.sprinbootcourse.persistence.education.model.onetoone;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

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
