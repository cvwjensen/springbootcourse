package dk.lundogbendsen.sprinbootcourse.persistence.gamer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.Repository;

public interface GamerRepository extends MongoRepository<Gamer, String> {
}
