package dk.lundogbendsen.urlshortener.repository;

import dk.lundogbendsen.urlshortener.model.Token;
import dk.lundogbendsen.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    List<Token> findAllByUser(User user);
    Optional<Token> findByTokenAndUser(String token, User user);
    Optional<Token> findByTokenAndProtectToken(String token, String protectToken);
    void deleteAllByUser(User user);
    void deleteByTokenAndUser(String token, User user);
}
