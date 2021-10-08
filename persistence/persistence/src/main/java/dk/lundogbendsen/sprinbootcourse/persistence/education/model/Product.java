package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany(mappedBy = "products")
    List<Order> orders;
}
