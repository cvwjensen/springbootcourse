package dk.lundogbendsen.sprinbootcourse.persistence.education;

import dk.lundogbendsen.sprinbootcourse.persistence.education.model.*;
import dk.lundogbendsen.sprinbootcourse.persistence.education.repository.CourseRepository;
import dk.lundogbendsen.sprinbootcourse.persistence.education.repository.QBExampleRepository;
import dk.lundogbendsen.sprinbootcourse.persistence.education.repository.StudentRepository;
import dk.lundogbendsen.sprinbootcourse.persistence.education.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

// Alternative 1: Use pure Spring
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {PersistenceApplication.class})

// Alternative 2: Use SpringBootTest
//@SpringBootTest

// Alternative 3: Use Slice
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BasicQueriesTest {

    @Autowired
    EntityManager em;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    QBExampleRepository qbExampleRepository;

    final Student josh = Student.builder().name("Josh").build();
    final Student jane = Student.builder().name("Jane").build();
    final Student tom = Student.builder().name("Tom").build();
    final Student anna = Student.builder().name("Anna").build();

    final Teacher johnson = Teacher.builder().name("Johnson").build();
    final Teacher smith = Teacher.builder().name("Smith").build();
    final Teacher kayne = Teacher.builder().name("Kayne").build();

    final Course art = Course.builder().subject("art").points(10)
            .teacher(johnson)
            .students(Set.of(josh, jane, tom)).build();
    final Course philosophy = Course.builder().subject("philosophy").points(10)
            .teacher(smith)
            .students(Set.of(tom, anna, jane)).build();
    final Course math = Course.builder().subject("math").points(10)
            .teacher(kayne)
            .students(Set.of(josh, tom, anna)).build();

    final QBExample qbe1 = QBExample.builder().p1("string1").p2(1L).p3(List.of("s1", "s2", "s3")).build();
    final QBExample qbe2 = QBExample.builder().p1("string2").p2(2L).p3(List.of("s4", "s5", "s6")).build();
    final QBExample qbe3 = QBExample.builder().p1("string3").p2(3L).p3(List.of("s1", "s2", "s6")).build();

    @BeforeEach
    public void init() {
        studentRepository.saveAll(List.of(josh, anna, jane, tom));
        teacherRepository.saveAll(List.of(johnson, smith, kayne));
        courseRepository.saveAll(List.of(art, philosophy, math));
        courseRepository.flush();
        em.clear();
        qbExampleRepository.saveAll(List.of(qbe1, qbe2, qbe3));
    }

    @Test
//    @Rollback(value = false)
    public void findAllCourses() {
        final List<Course> all = courseRepository.findAll();
        assertEquals(3, all.size());
        assertThat(List.of("art", "philosophy", "math"), containsInAnyOrder(all.get(0).getSubject(), all.get(1).getSubject(), all.get(2).getSubject()));
    }

    @Test
    public void findAllStudentsPaginatedAndSorted() {
        Pageable page = PageRequest.of(3, 1, Sort.sort(Student.class).by(Student::getName).ascending());
        Page<Student> all = studentRepository.findAll(page);

        assertEquals(4, all.getTotalElements());
        assertEquals(4, all.getTotalPages());
        assertEquals("Tom", all.getContent().get(0).getName());
    }


    @Test
    public void getOneTest() {
        Long id = johnson.getId();
        final Teacher teacher = teacherRepository.getOne(id);
        assertEquals("Johnson", teacher.getName());
        assertEquals(1, teacher.getTeaches().size());
    }

    @Test
    public void getOne_NonExistingId_Test() {
        Long id = 100L;
        Teacher teacher = teacherRepository.getOne(id);
        assertNotNull(teacher);
        try {
            teacher.getTeaches();
            fail();
        } catch (EntityNotFoundException e) {

        }
    }

    @Test
    public void findById_Test() {
        Long id = johnson.getId();
        final Optional<Teacher> teacher = teacherRepository.findById(id);
        assertTrue(teacher.isPresent());
    }

    @Test
    public void countCourses() {
        final long count = courseRepository.count();
        assertEquals(3, count);
    }

    @Test
//    @Rollback(value = false)
    public void teacherJohnsonsCourses() {
        final Teacher teacher = teacherRepository.findById(johnson.getId()).get();
        assertEquals(art.getId(), teacher.getTeaches().stream().findFirst().get().getId());
    }

    @Test
    public void getStudentJoshsCourses() {
        final Student student = studentRepository.getOne(josh.getId());
        assertEquals(2, student.getCourses().size());
    }

    @Test
//    @Rollback(value = false)
    public void deleteArtCourse() {
        courseRepository.delete(art);
        courseRepository.flush();
    }

    @Test
    public void deleteTeacherJohnsonV1() {
        teacherRepository.delete(johnson);
        try {
            teacherRepository.flush();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
//    @Rollback(value = false)
    public void deleteTeacherJohnson() {
        Teacher teacherJohnson = teacherRepository.getOne(johnson.getId());
        teacherJohnson.getTeaches().forEach(course -> course.setTeacher(null));
        teacherRepository.delete(teacherJohnson);
        teacherRepository.flush();
    }

    @Test
//    @Rollback(value = false)
    public void deleteStudent() {
        final Student student = studentRepository.getOne(anna.getId());
        student.getCourses().forEach(course -> {
            Set<Student> students = new HashSet<>(course.getStudents());
            students.remove(student);
            course.setStudents(students);
        });
        studentRepository.delete(student);
        studentRepository.flush();
    }

    @Test
    public void createStudent_addToCourses() {
        final Student jim = Student.builder().name("Jim").build();
        final Course courseArt = courseRepository.findById(art.getId()).get();
        courseArt.getStudents().add(jim);
        courseRepository.save(courseArt);
    }

}
