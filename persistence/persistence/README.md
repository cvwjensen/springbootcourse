# Persistence

## Exercises for Persistence

Prerequisite for these exercises is a new Springboot project with the following dependencies:

- spring-boot-starter-data-jpa
- h2 database

### Exercise 1: create a Domain model for the Education case
In this exercise you are going to create 3 domain classes representing Student, Course and Teacher.

A Student has a name and a list of Courses.
A Course has a subject, a list of Students and a Teacher.
A Teacher has a name and a list of Courses.

- Create the 3 domain classes. Use Lombok @Data and @Builder annotation.

#### Solution
```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String subject;
    private Integer points;
    private Teacher teacher;
    private Set<Student> students;
}
```

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String name;
    private Set<Course> courses;
}
```

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    private String name;
    private Set<Course> teaches;
}
```

### Exercise 2: Upgrade the Domain model to an Entity model

- Update the 3 domain classes to Entities by adding the @Entity annotation and a Long id annotated with @Id and @GeneratedValue.
- Add appropriate relationship annotations on the entities.

#### Solution
```java
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private String subject;
    private Integer points;
    @ManyToOne
    private Teacher teacher;
    @ManyToMany
    private Set<Student> students;
}
```

```java
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "students")
    private Set<Course> courses;
}
```

```java
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany
    private Set<Course> teaches;
}
```

### Exercise 3: Run the application and see the DDL generated
At this point we have an Entity Model, and we now would like to see what DDL is generated. Also notice that Hibernate understands dependencies between tables, and will drop tables "bottoms-up", and create them "top-down".

In order to see the SQL output, add this line to your application configuration:

`spring.jpa.show-sql=true`

Then run the application and inspect the logging. Notice how it starts out dropping any table if it exists. It cleans everything up and starts out fresh.
This is very nice in the development phase on your local machine where you make a lot of changes. But when the Entity model starts stabilizing or when you deploy to a test/prod environment, you probably don't want the application to wipe the database on every start. 

Since Hibernate is the implementation underneath JPA in this setup, you control DDL behaviour with this configuration:

`spring.jpa.hibernate.ddl-auto=none`



#### Solution
```sql
Hibernate: drop table if exists course CASCADE 
Hibernate: drop table if exists course_students CASCADE 
Hibernate: drop table if exists student CASCADE 
Hibernate: drop table if exists teacher CASCADE 
Hibernate: drop table if exists teacher_teaches CASCADE 
Hibernate: drop sequence if exists hibernate_sequence
Hibernate: create sequence hibernate_sequence start with 1 increment by 1
Hibernate: create table course (id bigint not null, points integer, subject varchar(255), teacher_id bigint, primary key (id))
Hibernate: create table course_students (courses_id bigint not null, students_id bigint not null)
Hibernate: create table student (id bigint not null, name varchar(255), primary key (id))
Hibernate: create table teacher (id bigint not null, name varchar(255), primary key (id))
Hibernate: create table teacher_teaches (teacher_id bigint not null, teaches_id bigint not null)
Hibernate: alter table teacher_teaches add constraint UK_bwumw525p0iaav79x9lixhity unique (teaches_id)
Hibernate: alter table course add constraint FKsybhlxoejr4j3teomm5u2bx1n foreign key (teacher_id) references teacher
Hibernate: alter table course_students add constraint FK532dg5ikp3dvbrbiiqefdoe6m foreign key (students_id) references student
Hibernate: alter table course_students add constraint FKh6irfl8rj4jgb3xrlyxsr2bdk foreign key (courses_id) references course
Hibernate: alter table teacher_teaches add constraint FK5w66phdtydqm3hg7lki9md8il foreign key (teaches_id) references course
Hibernate: alter table teacher_teaches add constraint FKa3ccrnysoopdf4jtulh9ga2rn foreign key (teacher_id) references teacher
```

### Exercise 4: Make repositories for the entity model

- Make 3 repository interfaces named after the 3 entities extending the JpaRepository interface.

#### Solution
```java
public interface CourseRepository extends JpaRepository<Course, Long> {}
```
```java
public interface StudentRepository extends JpaRepository<Student, Long> {}
```
```java
public interface TeacherRepository extends JpaRepository<Teacher, Long> {}
```

### Exercise 5: Make unit tests (@DataJpaTest) to try out the repositories
- Make a new test class for testing the repositories
- Add the three repositories using @Autowired
- Also add an EntityManager - this is necessary when running unit test to be able to force Hibernate to remove entities from memory and reload them upon request. Otherwise entity relations are not hydrated.
  Use `@Autowired EntityManager em;`
- Define the following entities:
```java
    final Student josh = Student.builder().name("Josh").build();
    final Student jane = Student.builder().name("Jane").build();
    final Student tom = Student.builder().name("Tom").build();
    final Student anna = Student.builder().name("Anna").build();

    final Teacher johnson = Teacher.builder().name("Johnson").build();
    final Teacher smith = Teacher.builder().name("Smith").build();
    final Teacher kayne = Teacher.builder().name("Kayne").build();

    final Course art = Course.builder().subject("art").points(5)
            .teacher(johnson)
            .students(List.of(josh, jane, tom)).build();
    final Course philosophy = Course.builder().subject("philosophy").points(10)
            .teacher(smith)
            .students(List.of(tom, anna, jane)).build();
    final Course math = Course.builder().subject("math").points(15)
            .teacher(kayne)
            .students(List.of(josh, tom, anna)).build();
```
- Make a test method that saves the entities using the repositores before each test.
- use courseRepository to flush pending database operations.
- Use entityManager to clear the loaded entities, so they will be reloaded in the following test, and thereby re-hydrated.
- Run the test and verify that it succeeds.

#### Solution
```java
@DataJpaTest
public class EducationTests {
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

    final Course art = Course.builder().subject("art").points(10)
            .teacher(johnson)
            .students(List.of(josh, jane, tom)).build();
    final Course philosophy = Course.builder().subject("philosophy").points(10)
            .teacher(smith)
            .students(List.of(tom, anna, jane)).build();
    final Course math = Course.builder().subject("math").points(10)
            .teacher(kayne)
            .students(List.of(josh, tom, anna)).build();


    @Test
    public void createAllAssociations() {
        courseRepository.saveAll(List.of(art, philosophy, math));
        studentRepository.saveAll(List.of(josh, anna, jane, tom));
        teacherRepository.saveAll(List.of(johnson, smith, kayne));
        courseRepository.flush();
        em.clear();
    }
}
```

### Exercise 6: Convert test to @BeforeEach
The @Test in previous exercise was just for warming up. To see if we setup the entities right. 
In this exercise you'll convert the test to an initializing method that runs before every test.


- Change the annotation to @BeforeEach
- Change the name to init().

By doing this you now have a solid, well-known data foundation for coming tests. 

#### Solution
```java
@BeforeEach
public void init() {
    courseRepository.saveAll(List.of(art, philosophy, math));
    studentRepository.saveAll(List.of(josh, anna, jane, tom));
    teacherRepository.saveAll(List.of(johnson, smith, kayne));
    courseRepository.flush();
    em.clear();
}
```

## Queries

We now have a test class setup with test data we can use for experimenting with queries.

### Exercise 1: Basis Queries - all courses
In this exercise we will familiarize our self with the queries offered by the repositories in their current state.

- Make a test for finding all courses.
- Use the courseRepository findAll() to fetch all courses.
- Assert that you get 3 and that their subjects are art, philosophy, and math

#### Solution
```java
    @Test
    public void findAllCourses() {
        final List<Course> all = courseRepository.findAll();
        assertEquals(3, all.size());
        assertThat(List.of("art", "philosophy", "math"), containsInAnyOrder(all.get(0).getSubject(), all.get(1).getSubject(), all.get(2).getSubject()));
    }
```


### Exercise 2: Basis Queries - Sort Students, paginate and retrieve last page
JpaRepositories support Pagination and Sorting. Some of the built-in queries like findAll takes a Pagable, which encapsulates page page size and sorting.


A Pagable can be built with like this:

`Pageable page = PageRequest.of(3, 1, Sort.sort(Student.class).by(Student::getName).ascending());`

- Make a test for fetching last page of Students with page size 1, sorted by student name ascending.
- Assert that you the students name is Tom.

#### Solution
```java
    @Test
    public void findAllStudentsPaginatedAndSorted() {
        Pageable page = PageRequest.of(3, 1, Sort.sort(Student.class).by(Student::getName).ascending());
        Page<Student> all = studentRepository.findAll(page);

        assertEquals(4, all.getTotalElements());
        assertEquals(4, all.getTotalPages());
        assertEquals("Tom", all.getContent().get(0).getName());
    }
```

### Exercise 3: Basis Queries - get by ID
You can get an Entity directly if you know the ID.

- Make a test for fetching Teacher Johnson. Use the getOne().
- Assert that the fetched teachers name is Johnson.

#### Solution
```java
    @Test
    public void getOneTest() {
        Long id = johnson.getId();
        final Teacher teacher = teacherRepository.getOne(id);
        assertEquals("Johnson", teacher.getName());
    }
```

### Exercise 4: Basis Queries - get by non-existing ID
What happens if you getOne with a non-existing ID? You get a handle that proxies a null value.

- Make a test for fetching Teacher Johnson. Use the getOne(100L).
- Assert that the returned value is not null (assertNotNull()).
- try to get the set of courses from the teacher. (catch EntityNotFoundException)
- Fail() if that succeed. 

#### Solution
```java
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
```

### Exercise 5: Basis Queries - findById
There is another way to get an entity by ID. Use the findById(). It returns an Optional which you can use to ask if there is an entity or not.

Difference to getOne() is that findById is EAGER, whereas the getOne is LAZY. EAGER means that an actual database query is performed immediately, while the LAZY will do it when a property is accessed the first time. It will then throw an EntityNotFoundException if it does not exists.

- Make a test for fetching Teacher Johnson. Use the getOne(johnson.findById()).
- Assert that the returned value is present using the Optional.isPresent()

#### Solution
```java
    @Test
    public void findById_Test() {
        Long id = johnson.getId();
        final Optional<Teacher> teacher = teacherRepository.findById(id);
        assertTrue(teacher.isPresent());
    }
```

### Exercise 6: Basis Queries - Counting
You can count entities using count() method.

- Make a test for counting courses.
- Use the courseRepository.count() to fetch the count.
- Assert that there are 3 courses.

#### Solution
```java
    @Test
    public void countCourses() {
        final long count = courseRepository.count();
        assertEquals(3, count);
    }
```

### Exercise 7: Basis Queries - Navigation
In this exercise we will see how navigation works. We load a Student, find a Course, and from that the Teacher.

- Make a test case for navigation.
- Load the Student Tom.
- Get the art Course from Toms courses (Tip: use stream().filter().findFirst())
- Get the Teacher from that Course.
- Assert that the Teachers name is Johnson.

#### Solution
```java
    @Test
    public void navigateFromStudentToTeacher() {
        final Student student = studentRepository.getOne(tom.getId());
        final Course course = student.getCourses().stream().filter(c -> c.getSubject().equals("art")).findFirst().get();
        final Teacher teacher = course.getTeacher();
        assertEquals("Johnson", teacher.getName());
    }
```
## Modifying operations
Reading is one thing - updating another. 
There are some things to consider when updating entitites especially if they are related to other entities.

The things to consider are this: foreign key constraints.

When deleting entities, we must ensure that all references are deleted as well.

When updating relations, we must update any uni-directional relation separately.

Furthermore, when Hibernate loads an Entity from the database, it also fetches its relations. So loading a Course, the Teacher and Students are also loaded. 
The Course is said to be hydrated. But collections are loaded into an immutable Collection making it impossible to add or remove relations.

The trick is to make a new collection, copy over any relation to remain, and replace the original. We'll see a few examples of this.


### Exercise 1: Basis Queries - Delete Course
We'll try to delete a course in this exercise. 

- Make a test for deleting the Art course.
- use courseRepository.delete() to delete it.
- use courseRepository.flush() to force Hibernate to execute the queries in the transaction.
 
Even tough there is a Teacher and some Students associated with the Course, the delete succeeds because the relations are owned by the Course, and therefore are handled when the Course is deleted.

#### Solution
```java
    @Test
    public void deleteArtCourse() {
        courseRepository.delete(art);
        courseRepository.flush();
    }
```

### Exercise 2: Basis Queries - Delete Teacher - version 1.
We'll try to delete a Teacher in this exercise. 

- Make a test for deleting the the Teacher Johnson.
  
- use teacherRepository.delete() to delete it.
- use teacherRepository.flush() to force Hibernate to execute the queries in the transaction.
 
This will fail because of referential integrity (the relation to a Course is not owned by the Teacher).

#### Solution
```java
    @Test
    public void deleteTeacherJohnsonV1() {
        teacherRepository.delete(johnson);
        try {
            teacherRepository.flush();
            fail();
        } catch (Exception e) {
        }
    }
```

### Exercise 3: Basis Queries - Delete Teacher - version 2.
We'll try to delete a Teacher in this exercise. But this time we will remove associations first.

- Make a test for deleting the the Teacher Johnson.
- Load the Teacher Johnson.
- Update his courses by setting their teacher to null.
- use teacherRepository.delete() to delete it.
- use teacherRepository.flush() to force Hibernate to execute the queries in the transaction.
 
#### Solution
```java
    @Test
    public void deleteTeacherJohnson() {
        Teacher teacherJohnson = teacherRepository.getOne(johnson.getId());
        teacherJohnson.getTeaches().forEach(course -> course.setTeacher(null));
        teacherRepository.delete(teacherJohnson);
        teacherRepository.flush();
    }
```

### Exercise 4: Basis Queries - Delete Student
Students are in a ManyToMany relationship with Courses. We'll try delete one.

- Make a test for deleting a Student.
- Load the Student Anna.
- Iterate over her Courses and remove her from them.
- Delete the Student.

#### Solution
```java
        final Student student = studentRepository.getOne(anna.getId());
        student.getCourses().forEach(course -> {
            Set<Student> students = new HashSet<>(course.getStudents());
            students.remove(student);
            course.setStudents(students);
        });
        studentRepository.delete(student);
        studentRepository.flush();
```

### Exercise 5: Add Student and assign to Course
In this exercise we will add a new Student and put into a Course.

- Make a new test for Creating a Student (Jim).
- Load the Art course and add the Student.
- Load the Math course and add the Student.
- Save the courses and the student.
- Flush the course repository to force database write.
- Load the student using the studentRepository.
- Assert that the student have 2 courses in his courses collection.

#### Solution
```java
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
```

### Exercise 6: Remove Student from Course
In this exercise we will remove Jane from the Course Philosophy.

- Make a test case for removing a Student from a Course.
- Load the Course "Philosophy".
- Load the Student Jane, and assert that she have 2 courses.
- Remove Jane from the set of Students.
- Save the Course.
- Flush.
- Use EntityManager to refresh the Student (force re-hydratation).
- Assert that the student now only have 1 course in her list.

#### Solution
```java
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
```

## Customizing the Repositories: Property Expressions
This is as far as we can get using the repositories out of the box. We got at lot of functionality, but they only cover basic stuff.

Repositories support a other options for querying and updating the database.

In this section we will look at Property Expressions.

Property Expressions are methods you add on the Repository whos naming and signature follow a certain convention.


### Exercise 1: find all courses by a Student
We will use a Property Expression query for finding all courses of a given student.

- Open the CourseRepository class.
- Add a new method: `List<Course> findByStudents(Student student);`
- Make a test that uses the new method for finding the courses of Student Anna.
- Assert that the list of courses consists of philosophy and math.

#### Solution
```java
    List<Course> findByStudents(Student student);
```

```java
    @Test
    public void findCoursesByStudent() {
        final Student student = studentRepository.findById(anna.getId()).get();
        final List<Course> coursesByStudentsContaining = courseRepository.findByStudents(student);
        assertThat(List.of("philosophy", "math"), containsInAnyOrder(coursesByStudentsContaining.get(0).getSubject(), coursesByStudentsContaining.get(1).getSubject()));
    }
```

### Exercise 2: find all courses by a a list of Students
This test is a variation of the previous. But instead of finding courses for 1 student, we will find for a list of students. This time we need to ensure that the result only return unique rows by using Distinct.

- Open the CourseRepository class.
- Add a new method: `List<Course> findDistinctByStudentsIn(List<Student> students);`
- Make a test that uses the new method for finding the courses of Student Anna and Tom.
- Assert that the list of courses consists of art, philosophy and math.

#### Solution
```java
    List<Course> findDistinctByStudentsIn(List<Student> students);
```

```java
    @Test
    public void findCoursesByListOfStudents() {
        final Student student1 = studentRepository.findById(anna.getId()).get();
        final Student student2 = studentRepository.findById(tom.getId()).get();
        final List<Course> coursesByStudentsContaining = courseRepository.findDistinctByStudentsIn(List.of(student1, student2));
        assertThat(List.of("art", "philosophy", "math"), containsInAnyOrder(coursesByStudentsContaining.get(0).getSubject(), coursesByStudentsContaining.get(1).getSubject(), coursesByStudentsContaining.get(2).getSubject()));
    }
```

### Exercise 3: Find courses points in a certain range
In the exercise we will make a finder that works on the basic attribute of an entity.

- Add a method to CourseRepository that finds courses by the points of the course if the point lies between a lower and an upper limit (two Integers) 
  (use Intellij to help out here. Start by typing "findAllBy" and let Intellij come to help. You can then continue with "PointsBetween" )
- Make a test case for finding courses by their points.
- Use the bew method to find all courses with points between 5 and 12.
- Assert that you get two courses back.

#### Solution
```java
    List<Course> findCoursesByPointsBetween(Integer start, Integer end);
```

```java
    @Test
    public void findCoursesWithPointsBetween() {
        final List<Course> coursesByPointsBetween = courseRepository.findCoursesByPointsBetween(5, 12);
        assertEquals(2, coursesByPointsBetween.size());
    }
```


### Exercise 4: Find By Points, Students and Teacher - and paginate and sort
In the exercise we will make a method on the courseRepository that work on several properties at once while at the same time paginates and sorts the result.

- Make a new method on courseRepository that is named after Points between, a Student and a Teacher. 
- Also add a Pagable to the method signatures argument list.
- Make a test for finding all courses with points between 5 and 12 that is attended by Anna, and tought by Smith. The result should be paginated with page size 1 and sorted ascending by subject.

**Tip:** 

A Pagination object can be constructed like this:
```java
PageRequest.of(0,1, Sort.by("subject").ascending())
```

#### Solution
```java
    List<Course> findCoursesByPointsBetweenAndStudentsAndTeacher(Integer start, Integer end, Student student, Teacher teacher, Pageable pageable);
```

```java
    @Test
    public void findCoursesWithPointsBetween_AndStudent_AndTeacher_AndPagination() {
        Student studentAnna = studentRepository.findById(anna.getId()).get();
        Teacher teacherSmith = teacherRepository.getOne(smith.getId());
        final List<Course> coursesByPointsBetween = courseRepository.findCoursesByPointsBetweenAndStudentsAndTeacher(5, 12, studentAnna, teacherSmith, PageRequest.of(0,1, Sort.by("subject").ascending()));
        assertEquals(1, coursesByPointsBetween.size());
    }
```


## Customizing the Repositories: Custom queries
As can be seen by previous section, the property expressions are quite flexible. But sometimes the names - while quite logical - gets a bit long and unreadable.

In the section we will investigate the custom queries using the @Query annotation. The Language used in @Query is JPQL, which resembles SQL, but with convenience on top to navigate relations.

### Exercise 1: rewrite the former property expression query
To be able to compare solutions, we rewrite the findCoursesByPointsBetweenAndStudentsAndTeacher query using @Query

The JPQL is very similar to SQL. You can use joins to query on relations. This we will do in this case.

- In Course repository make a method for fetching a list of course by the properties points, students and teacher. Call it something short and intuitive.
- Let is also accept a Pageable in order to paginate and sort the result.
- Make a test that uses the new method in the same way as previous exercise (#4).


#### Solution
```java
    @Query("select c from Course c inner join c.students s where c.points between ?1 and ?2 and s=?3 and c.teacher=?4")
    List<Course> listCourses(Integer start, Integer end, Student student, Teacher teacher, Pageable pageable);
```

```java
    @Test
    public void listCoursesWithPointsBetween_AndStudent_AndTeacher_AndPagination() {
        Student studentAnna = studentRepository.findById(anna.getId()).get();
        Teacher teacherSmith = teacherRepository.getOne(smith.getId());
        final List<Course> coursesByPointsBetween = courseRepository.listCourses(5, 12, studentAnna, teacherSmith, PageRequest.of(0,1, Sort.by("subject").ascending()));
        assertEquals(1, coursesByPointsBetween.size());
    }
```

### Exercise 2: Using attributes of related Entities
The @Query lets you make queries on related Entitites attributes - as opposed to Property Expressions that only lets you query on Relations.

In this exercise we will find students and order them by how many points their courses are worth. Furthermore we add Pageable to support the sort direction thereby letting you find top students or bottom students.

- Make a method on StudentRepository called "findTopStudents" that takes a Pageable as argument.
- Add a @Query to the method that 
  - joins Students with their Courses
  - selects Students and the sum of points of their courses (this sum should be aliased to "points")
  - groups them by student.
- Make a test case that get the topstudents using the new method. 
- Use a Pageable that takes out page 0, page size 10 and sorts by points descending.
- Assert that the Students in the list are ordered by how many points they are "worth".


#### Solution
```java
    @Query("select s, sum(c.points) as points from Student s inner join s.courses c group by s")
    List<Student> findTopStudents(Pageable pageable);
```

```java
    @Test
    public void findTopStudent_ByCoursePoints() {
        List<Student> students = studentRepository.findTopStudents(PageRequest.of(0, 10, Sort.by("points").descending()));
        assertEquals(30, students.get(0).getCourses().stream().mapToInt(Course::getPoints).sum());
        assertEquals(25, students.get(1).getCourses().stream().mapToInt(Course::getPoints).sum());
        assertEquals(20, students.get(2).getCourses().stream().mapToInt(Course::getPoints).sum());
        assertEquals(15, students.get(3).getCourses().stream().mapToInt(Course::getPoints).sum());
    }
```


## Custom implementations of Repository Methods

Some times it is not enough with @Query or Property Expressions. Or maybe you just want to control everything your self. Or maybe it is impossible to express the query in a single statement.

For all of these reasons you can take matters in your own hand and implement a custom query which still conforms with the Repository paradigm.

### Exercise 1: Make a custom Repository implementation getting students by teacher name
Our goal is the extend the existing TeacherRepository with queries that are not easy or convenient to express as Property Expressions or @Query. 
First we need to create a new interface with the new method. Then we make an implementation of that interface. At this point we have still done nothing for the original TeacherRepository.
But now we modify the TeacherRepository by extending the new interface. Thereby the TeacherRepository have the methods of the interface.

By convention the Spring looks for an implementation of the interface by looking for a class with the name of the interface postfixed with the word 
"Impl".



- Make a new Interface called `TeacherRepositoryCustom`.
- Add a method `List<Student> getStudentNames(String teacherName)`.
---
- Make a new class `TeacherRepositoryCustomImpl` - named excatly like the repository with the 'Impl' postfix.
- Implement the `TeacherRepositoryCustom` interface.
- Annotate the class with @Component to enable Autowiring.
- Autowire in the EntityManager to get a Database handle.
- Make a query by calling EntityManager.createQuery() with JPQL: `select s from Course c inner join c.students s where c.teacher.name=:name` and Student.class.
- add the teacherName to the query using query.setParameter. The name of the parameter is "name" as can be seen in the JPQL.
- Execute the query and return the result.
---
- Modify the `TeacherRepository` and let it extend the new `TeacherRepositoryCustom` interface.
---
- Make a test case that uses the new method for teacher Johnson.
- Assert that the students are josh, jane, and tom.

#### Solution
_Make the new interface:_
```java
public interface TeacherRepositoryCustom {
    List<Student> getStudents(String teacherName);
}
```

---
_Implement the new interface - make it a Spring bean in other to use Autowiring._
```java
@Component
public class TeacherRepositoryCustomImpl implements TeacherRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<Student> getStudents(String teacherName) {
        final Query query = entityManager.createQuery("select s from Course c inner join c.students s where c.teacher.name=:name", Student.class);
        query.setParameter("name", teacherName);
        return query.getResultList();
    }
}
```

---
_Extend the original interface to inherit the new interface methods. Spring will look for the implementation by naming convention._
```java
public interface TeacherRepository extends JpaRepository<Teacher, Long>, TeacherRepositoryCustom {
}

```

---
_Use the new methods._
```java
@Test
public void testCustomImplementation() {
    final List<Student> johnson = teacherRepository.getStudents("Johnson");
    assertEquals(3, johnson.size());
}
```

### Exercise 2