# Person-Service-Starter

## Exercises
We are going to turn an existing project into a SpringBoot starter project.

The starting point of a starter will be an empty maven project. Step by step we enhance this project and end up with 
your very own spring-boot-starter for the person-service project.

A prerequisite is to get the person-service from github and build it.




### Exercise 1: Get and build person-service.
You need to get the person-service downloaded to your PC, and then build it.

Alternative 1 - using the Terminal

- Use a terminal to clone the github repo: https://github.com/cvwjensen/person-service.git
- cd in to the project folder and execute `mvn install`.

Alternative 2 - using the IDE

- Use your IDE to create a new project from VCS and pass the repo address https://github.com/cvwjensen/person-service.git.
- Use the IDE to execute the maven Install command on the project.


### Exercise 2: Create a maven project
With the person-service installed in your local maven repo, it is time to create the actual project that will become the 
starter for person-service.

Use your IDE to create a maven project using your latest JDK.

This gives you a completely empty project with no java files, no tests and no dependencies - but it is a
maven project, and we are ready to rock.


### Exercise 3: Add dependencies to project
In this step we add 2 dependencies. One for Spring and one for Person-Service. The 

- Include the dependency for spring-boot-starter
  - hint: copy the dependency from an existing SpringBoot project in the springbootcourse repo.

In this dependency we get Spring core modules along with all the @ConditionalNNN annotations required for creating SpringBoot starters.

- Include the dependency for person-service

The person-service maven coordinates are:
```
    <groupId>org.example</groupId>
    <artifactId>person-service</artifactId>
    <version>1.0-SNAPSHOT</version>
```

#### Solution

Content of the `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>person-service-starter-demo</artifactId>
  <version>1.0-SNAPSHOT</version>

  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <version>2.7.2</version>
    </dependency>
    <dependency>
      <groupId>org.example</groupId>
      <artifactId>person-service</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
</project>
```

### Exercise 4: Add Configuration class to produce Spring Bean of Person-Service
With the project foundation in place it is time to do some actual coding. We should now create a class with the
@Configuration annotation on top and with a method that returns a new instance of PersonService.

- Create a class PersonServiceConfiguration an annotate with @Configuration
- Add a method that produces a Spring Bean of Person Service
  - Hint: use @Bean annotation.

#### Solution
```
package com.example.personservicestarter;

import dk.lundogbendsen.springbootcourse.api.personservice.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonServiceConfiguration {
    @Bean
    public PersonService personService() {
        return new PersonService();
    }
}
```

### Exercise 5: Make bean creation conditional
Spring-Boot is all about being a good citizen, and that means to back off gracefully if required. 
If the code is used as it stands, it WILL produce a PersonService if included and the client have no option
to create it itself. Therefore - in the spirit of Spring-Boot, we will add some conditions on the bean creation
to give the client a chance of taking control.

- On the class level add a @ConditionalOnClass(PersonService.class)
- On the method level add a @ConditionalOnMissingBean(PersonService.class)

In this way a bean is only created IF a) the PersonService is actually on the classpath and b) the client did not
create one.


#### Solution
```
package com.example.personservicestarter;

import dk.lundogbendsen.springbootcourse.api.personservice.service.PersonService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(PersonService.class)
public class PersonServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean(PersonService.class)
    public PersonService personService() {
        return new PersonService();
    }

}
```


### Exercise 6: The spring.factories files
The last element of a Spring-Boot starter is the file spring.factories. This file is processed by SpringBoot AutoConfiguration
and will be sure to include your beans in the client project.

- create a new file under `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
Add a line with this content: `com.example.personservicestarter.PersonServiceConfiguration` (or whatever your configuration file is called)

#### Solution

Content of `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`:

```
com.example.personservicestarter.PersonServiceConfiguration
```

### Exercise 7: Build and install the project
Use maven to build and install the person-service-starter project using either the terminal or the IDE

#### Solution

```
cd into person-service-starter project.
execute `mvn install`
```


### Exercise 8: Try out the starter in a new SpringBoot project
Now you are done creating the starter - it is time to test it in a real project.

- Create a Naked SpringBoot project.
- Add a dependency to the person-service-starter.
- Extract the PersonService bean and test it.

#### Solution

```java
package com.example.demo;

import dk.lundogbendsen.springbootcourse.api.personservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Demo1Application {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(Demo1Application.class, args);
        final PersonService personService = context.getBean(PersonService.class);
        System.out.println("personService.list() = " + personService.list());
    }
}
```