package dk.lundogbendsen.sprinbootcourse.persistence.education.repository;

import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Course;
import dk.lundogbendsen.sprinbootcourse.persistence.education.model.Student;
import dk.lundogbendsen.sprinbootcourse.persistence.education.model.StudentCourseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCoursesByStudentsIn(List<Student> students);

    @Query(value = "select distinct c.id from Course c inner join c.students s where s.name in :names")
    List<Long> findCourseIdsByStudentNames(@Param("names") List<String> studentNames);

    @Query(value = "select distinct c from Course c inner join c.students s where s.name in :names")
    List<Course> findCoursesByStudentNames(@Param("names") List<String> studentNames);

    @Query("select new dk.lundogbendsen.sprinbootcourse.persistence.education.model.StudentCourseCount(s.name, count(s.name)) from Course c inner join c.students s group by s.id")
    List<StudentCourseCount> findTopStudents();




    List<Course> findCoursesByPointsBetween(Integer start, Integer end);
}
