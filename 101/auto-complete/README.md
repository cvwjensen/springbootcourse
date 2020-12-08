# Auto-Complete

## Exercies

### Exercise 1: Find out where the auto-completion for server.port comes from
Hint: search for files with the name spring-configuration-metadata.json in all places


#### Solution
1. Open the file spring-boot-autoconfigure-2.4.0.jar!/META-INF/spring-configuration-metadata.json


### Exercise 2: Create your own META-INF/spring-configuration-metadata.json
Create a @Configuration/@ConfigurationProperties("app.mail") annotated class with the following properties:

    String smtp-server
    int port
    String username
    String password

Hint: Make sure it has getters and setters
Hint: Enable Annotation Configuration Processing in the IDE


#### Solution
1. Enable Annotation Configuration Processing in the IDE.
- Intellij:
Open preferences. Search for "annotation processing". Check the box "Enable Annotation Processing".
- Eclipse:
Right click on the project and select Properties.
Open Java Compiler -> Annotation Processing. Check "Enable annotation processing".
2. Create a class like: 101/auto-complete/src/main/java/dk/lundogbendsen/autocomplete/MailConfig.java
3. Clean and build the project.
4. Open the file target/classes/META-INF/spring-configuration-metadata.json
5. in application.properties verify that your properties now have autocomplete support.



### Exercise 3: Make the default value of the port property 25.

#### Solution
1. just set the default value for the field port=25
