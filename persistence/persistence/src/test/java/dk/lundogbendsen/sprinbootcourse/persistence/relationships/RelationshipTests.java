package dk.lundogbendsen.sprinbootcourse.persistence.relationships;

import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Department;
import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Employee;
import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Rifle;
import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Soldier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@DataJpaTest
public class RelationshipTests {
    @Autowired
    EntityManager entityManager;

    @Test
    public void oneToMany() {
        final Employee employee = new Employee();
        final Department department1 = new Department();
//        employee.setDepartment(department1);


        entityManager.persist(employee);
//        entityManager.persist(department1);
        entityManager.flush();

        entityManager.remove(employee);
        entityManager.flush();
    }

    @Test
    public void oneToOne() {
        Rifle rifle1 = new Rifle();
        final Soldier soldier = new Soldier();
        rifle1.setSoldier(soldier);
        entityManager.persist(soldier);
        entityManager.persist(rifle1);
        entityManager.flush();

        final Soldier soldier1 = entityManager.find(Soldier.class, soldier.getId());
        rifle1 = entityManager.find(Rifle.class, soldier.getId());

    }
}
