package dk.lundogbendsen.sprinbootcourse.persistence.education.model.manytomany;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany(mappedBy = "products")
    Set<Order> orders;
}
