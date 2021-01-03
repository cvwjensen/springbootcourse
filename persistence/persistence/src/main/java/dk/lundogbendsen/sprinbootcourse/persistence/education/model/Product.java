package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany
    List<Order> orders;
}
