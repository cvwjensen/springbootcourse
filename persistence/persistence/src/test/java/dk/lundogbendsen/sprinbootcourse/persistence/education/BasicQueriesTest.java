package dk.lundogbendsen.sprinbootcourse.persistence.education;

import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Course;
import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Student;
import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Teacher;
import dk.lundogbendsen.sprinbootcourse.persistence.education.repository.CourseRepository;
import dk.lundogbendsen.sprinbootcourse.persistence.education.repository.StudentRepository;
import dk.lundogbendsen.sprinbootcourse.persistence.education.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BasicQueriesTest {

    @Autowired EntityManager em;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    final Student josh = Student.builder().name("Josh").build();
    final Student jane = Student.builder().name("Jane").build();
    final Student tom = Student.builder().name("Tom").build();
    final Student anna = Student.builder().name("Anna").build();

    final Teacher johnson = Teacher.builder().name("Johnson").build();
    final Teacher smith = Teacher.builder().name("Smith").build();
    final Teacher kayne = Teacher.builder().name("Kayne").build();

    final Course art = Course.builder().subject("art").points(5)
            .teacher(johnson)
            .students(Set.of(josh, jane, tom)).build();
    final Course philosophy = Course.builder().subject("philosophy").points(10)
            .teacher(smith)
            .students(Set.of(tom, anna, jane)).build();
    final Course math = Course.builder().subject("math").points(15)
            .teacher(kayne)
            .students(Set.of(josh, tom, anna)).build();

    @BeforeEach
    public void init() {
        studentRepository.saveAll(List.of(josh, anna, jane, tom));
        teacherRepository.saveAll(List.of(johnson, smith, kayne));
        courseRepository.saveAll(List.of(art, philosophy, math));
        courseRepository.flush();
        em.clear();
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
    public void navigateFromStudentToTeacher() {
        final Student student = studentRepository.getOne(tom.getId());
        final Course course = student.getCourses().stream().filter(c -> c.getSubject().equals("art")).findFirst().get();
        final Teacher teacher = course.getTeacher();
        assertEquals("Johnson", teacher.getName());
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
        studentRepository.save(jim);
        final Course courseArt = courseRepository.findById(art.getId()).get();
        courseArt.getStudents().add(jim);
        final Course courseMath = courseRepository.findById(art.getId()).get();
        courseMath.getStudents().add(jim);
        courseRepository.save(courseArt);
        courseRepository.flush();

        final Student student = studentRepository.getOne(anna.getId());
        assertEquals(2, student.getCourses().size());
    }

    @Test
    public void removeStudentFromCourse() {
        final Course course = courseRepository.findById(philosophy.getId()).get();
        Student student = studentRepository.findById(jane.getId()).get();
        assertEquals(2, student.getCourses().size());
        course.getStudents().remove(student);
        courseRepository.save(course);
        courseRepository.flush();
        em.refresh(student);
        assertEquals(1, student.getCourses().size());
    }


    @Test
    public void findCoursesByStudent() {
        final Student student = studentRepository.findById(anna.getId()).get();
        final List<Course> coursesByStudentsContaining = courseRepository.findByStudents(student);
        assertThat(List.of("philosophy", "math"), containsInAnyOrder(coursesByStudentsContaining.get(0).getSubject(), coursesByStudentsContaining.get(1).getSubject()));
    }

    @Test
    public void findCoursesByListOfStudents() {
        final Student student1 = studentRepository.findById(anna.getId()).get();
        final Student student2 = studentRepository.findById(tom.getId()).get();
        final List<Course> coursesByStudentsContaining = courseRepository.findDistinctByStudentsIn(List.of(student1, student2));
        assertThat(List.of("art", "philosophy", "math"), containsInAnyOrder(coursesByStudentsContaining.get(0).getSubject(), coursesByStudentsContaining.get(1).getSubject(), coursesByStudentsContaining.get(2).getSubject()));
    }

    @Test
    public void findCoursesWithPointsBetween() {
        final List<Course> coursesByPointsBetween = courseRepository.findCoursesByPointsBetween(5, 12);
        assertEquals(2, coursesByPointsBetween.size());
    }

    @Test
    public void findCoursesWithPointsBetween_AndStudent_AndTeacher_AndPagination() {
        Student studentAnna = studentRepository.findById(anna.getId()).get();
        Teacher teacherSmith = teacherRepository.getOne(smith.getId());
        final List<Course> coursesByPointsBetween = courseRepository.findCoursesByPointsBetweenAndStudentsAndTeacher(5, 12, studentAnna, teacherSmith, PageRequest.of(0,1, Sort.by("subject").ascending()));
        assertEquals(1, coursesByPointsBetween.size());
    }

    @Test
    public void listCoursesWithPointsBetween_AndStudent_AndTeacher_AndPagination() {
        Student studentAnna = studentRepository.findById(anna.getId()).get();
        Teacher teacherSmith = teacherRepository.getOne(smith.getId());
        final List<Course> coursesByPointsBetween = courseRepository.listCourses(5, 12, studentAnna, teacherSmith, PageRequest.of(0,1, Sort.by("subject").ascending()));
        assertEquals(1, coursesByPointsBetween.size());
    }

}
