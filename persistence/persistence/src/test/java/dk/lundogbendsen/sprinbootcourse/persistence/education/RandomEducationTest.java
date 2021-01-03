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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)

// Alternative 1: Use pure Spring
//@ContextConfiguration(classes = {PersistenceApplication.class})

// Alternative 2: Use SpringBootTest
//@SpringBootTest

// Alternative 3: Use Slice
@DataJpaTest
public class RandomEducationTest {

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
        johnson.setTeaches(Set.of(art));
        smith.setTeaches(Set.of(philosophy));
        kayne.setTeaches(Set.of(math));

        josh.setCourses(Set.of(math, art));
        jane.setCourses(Set.of(art, philosophy));
        tom.setCourses(Set.of(art, math));
        anna.setCourses(Set.of(math, philosophy));

        courseRepository.saveAll(List.of(art, philosophy, math));
        studentRepository.saveAll(List.of(josh, anna, jane, tom));
        teacherRepository.saveAll(List.of(johnson, smith, kayne));
        qbExampleRepository.saveAll(List.of(qbe1, qbe2, qbe3));
    }

    @Test
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
//        assertEquals("art", teacher.getTeaches().get(0).getSubject());
//        assertEquals(3, teacher.getTeaches().get(0).getStudents().size());
    }

    @Test
    public void getOne_NonExistingId_Test() {
        Long id = 100L;
        final Teacher teacher = teacherRepository.getOne(id);
        assertNull(teacher);
    }

    @Test
    public void findById_Test() {
        Long id = johnson.getId();
        final Optional<Teacher> teacher = teacherRepository.findById(id);
        assertNull(teacher.isPresent());
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
