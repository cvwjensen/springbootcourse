package dk.lundogbendsen.sprinbootcourse.persistence.gamer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
//@DataMongoTest
public class GamerTest {

    @Autowired
    GamerRepository gamerRepository;

    @Test
    public void testGetGamer() {
        Gamer gamer = new Gamer();
        gamer.setAlias("The Master");
        gamer.setAvatar("ABC");
        gamerRepository.save(gamer);

        final Gamer gamer1 = gamerRepository.findById(gamer.getId()).get();
        assertEquals("The Master", gamer1.getAlias());
    }
}
