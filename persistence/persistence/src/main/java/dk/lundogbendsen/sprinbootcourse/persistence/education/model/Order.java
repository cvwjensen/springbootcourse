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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany
    List<Product> products;
}
