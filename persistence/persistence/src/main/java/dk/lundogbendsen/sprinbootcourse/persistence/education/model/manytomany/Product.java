package dk.lundogbendsen.sprinbootcourse.persistence.education.model.manytomany;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
