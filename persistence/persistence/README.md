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
- Define the following entities:
```java
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
```
- Make a test method that saves the entities using the repositores before each test.
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
    }
}
```

### Exercise 6: Convert test to @BeforeEach
The @Test in previous exercise was just for warming up. To see if we setup the entities right. 
In this exercise you'll convert the test to an initializing method that runs before every test.

- Change the annotation to @BeforeEach
- Change the name to init().

By doing this you now have a solid, wellknown data foundation for coming tests. 

#### Solution
```java
@BeforeEach
public void init() {
    courseRepository.saveAll(List.of(art, philosophy, math));
    studentRepository.saveAll(List.of(josh, anna, jane, tom));
    teacherRepository.saveAll(List.of(johnson, smith, kayne));
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

- Make a test for fetching Teacher Johnson. Use the getOne(johnson.getId()).
- Assert that the returned value is null assertNull()

#### Solution
```java
    @Test
    public void getOne_NonExistingId_Test() {
        Long id = 100L;
        final Teacher teacher = teacherRepository.getOne(id);
        assertNull(teacher);
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
 
Even tough there are a Teacher and some Students associated with the Course, the delete succeeds because the relations are owned by the Course.

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
- Update his courses and set the teacher to null.
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

- Make a new test for Creating af Student.
- Load the Art course and add the Student.
- Load the Math course and add the Student.

