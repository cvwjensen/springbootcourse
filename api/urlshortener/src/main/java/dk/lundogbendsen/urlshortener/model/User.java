package dk.lundogbendsen.urlshortener.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    String username;
    String password;
}