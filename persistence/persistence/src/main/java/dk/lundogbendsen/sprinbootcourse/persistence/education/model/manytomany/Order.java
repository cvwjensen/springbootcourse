package dk.lundogbendsen.sprinbootcourse.persistence.education.model.manytomany;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany
    Set<Product> products;
}
