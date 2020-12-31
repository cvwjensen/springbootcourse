package dk.lundogbendsen.urlshortener.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    String token;
    String protectToken;
    String targetUrl;
    @ManyToOne
    User user;
}
