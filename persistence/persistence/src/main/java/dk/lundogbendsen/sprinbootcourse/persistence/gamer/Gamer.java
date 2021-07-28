package dk.lundogbendsen.sprinbootcourse.persistence.gamer;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Gamer {
    @Id
    @GeneratedValue
    private Long id;
    private String alias;
    private String avatar;
}
