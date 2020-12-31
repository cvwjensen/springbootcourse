package dk.lundogbendsen.urlshortener.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    String username;
    String password;
}
