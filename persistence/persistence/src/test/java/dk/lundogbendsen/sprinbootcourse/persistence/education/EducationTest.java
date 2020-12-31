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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)

// Alternative 1: Use pure Spring
//@ContextConfiguration(classes = {PersistenceApplication.class})

// Alternative 2: Use SpringBootTest
//@SpringBootTest

// Alternative 3: Use Slice
@DataJpaTest
public class EducationTest {

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
            .students(List.of(josh, jane, tom)).build();
    final Course philosophy = Course.builder().subject("philosophy").points(10)
            .teacher(smith)
            .students(List.of(tom, anna, jane)).build();
    final Course math = Course.builder().subject("math").points(10)
            .teacher(kayne)
            .students(List.of(josh, tom, anna)).build();

    final QBExample qbe1 = QBExample.builder().p1("string1").p2(1L).p3(List.of("s1", "s2", "s3")).build();
    final QBExample qbe2 = QBExample.builder().p1("string2").p2(2L).p3(List.of("s4", "s5", "s6")).build();
    final QBExample qbe3 = QBExample.builder().p1("string3").p2(3L).p3(List.of("s1", "s2", "s6")).build();

    @BeforeEach
    public void init() {
        studentRepository.saveAll(List.of(josh, anna, jane, tom));
        teacherRepository.saveAll(List.of(johnson, smith, kayne));
        courseRepository.saveAll(List.of(art, philosophy, math));
        qbExampleRepository.saveAll(List.of(qbe1, qbe2, qbe3));
    }


    @Test
    public void testBasicOperations() {
        final List<Course> all = courseRepository.findAll();
        assertEquals(3, all.size());

        final long count = courseRepository.count();
        assertEquals(3, count);
    }

    @Test
    public void testQueryByExampleSimple() {
        // Declare the Example template:
        Example<? extends Course> example = Example.of(Course.builder().subject("philosophy").build());
        // Ask the repository to find entities that matches the example
        final Optional<? extends Course> artCourse = courseRepository.findOne(example);
        assertEquals("philosophy", artCourse.get().getSubject());
    }
    @Test
    public void testQueryByExample() {
        final ExampleMatcher partialSubjectMatcher = ExampleMatcher.matchingAny()
                .withMatcher("p1", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING));
        Example<? extends QBExample> example = Example.of(QBExample.builder().p1("s").p2(0L).build(), partialSubjectMatcher);
        final List<? extends QBExample> all = qbExampleRepository.findAll(example);
        assertEquals(3, all.size());
        assertThat(List.of("string1", "string2", "string3"), containsInAnyOrder(all.get(0).getP1(), all.get(1).getP1(), all.get(2).getP1()));
    }

    @Test
    public void testCustomFinders() {
        final Optional<Student> student = studentRepository.findOne(Example.of(Student.builder().name("Josh").build()));
        final List<Course> coursesByStudentsContaining = courseRepository.findCoursesByStudentsIn(List.of(student.get()));
        assertThat(List.of("art", "math"), containsInAnyOrder(coursesByStudentsContaining.get(0).getSubject(), coursesByStudentsContaining.get(1).getSubject()));

        final List<Course> coursesByPointsBetween = courseRepository.findCoursesByPointsBetween(0, 100);
        assertEquals(3, coursesByPointsBetween.size());
    }

    @Test
    public void testByQuery() {
        final List<Long> courseIds = courseRepository.findCourseIdsByStudentNames(List.of("Josh", "Anna"));
        assertThat(List.of(art.getId(), math.getId(), philosophy.getId()), containsInAnyOrder(courseIds.get(0), courseIds.get(1), courseIds.get(2)));

        final List<Course> courses = courseRepository.findCoursesByStudentNames(List.of("Josh", "Anna"));
        assertEquals(3, courses.size());
        assertThat(List.of("art", "math", "philosophy"), containsInAnyOrder(courses.get(0).getSubject(), courses.get(1).getSubject(), courses.get(2).getSubject()));

        // Show projection onto a class
        final List<StudentCourseCount> studentCoursesCount = courseRepository.findTopStudents();
        System.out.println("studentCoursesCount = " + studentCoursesCount);
    }

    @Test
    public void testCustomImplementation() {
        final List<Student> johnson = teacherRepository.getStudents("Johnson");
        assertEquals(3, johnson.size());
    }

//    @Test
//    public void testStudentsAttendingCourse() {
//        final List<Student> students = studentRepository.findAllByCoursesContaining(philosophy);
//        assertEquals(3, students.size());
//    }

//    @Test
//    public void testCoursesToughtByTeacher() {
//        final List<Course> allByTeachesIsNull = courseRepository.findAllByTeacher(johnson);
//        assertEquals(1, allByTeachesIsNull.size());
//    }

//    @Test
//    public void testStudentsToughtByTeacher() {
//        final List<Student> students = studentRepository.findAllByCoursesTeacherName("Johnson");
//        assertEquals(3, students.size());
//    }


}
