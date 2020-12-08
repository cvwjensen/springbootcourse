# Configuration

## Exercises

### Exercise 1. Provide an external configuration file which overrides
app.configIdentifier=file:/application.properties

#### Solution
1. copy the src/main/resources/application.properties to the root folder of the project
2. edit the new file and set the key app.configIdentifier=file:/application.properties
3. run the application from the command line using mvn spring-boot:run



### Exercise 2. Use an environment variable to override
app.configIdentifier=env

#### Solution
1. declare an environment variable using set APP_CONFIGIDENTIFIER=env
2. run the application from the command line using mvn spring-boot:run



### Exercise 3. Activate the demo profile
Hint: the property controlling which profiles are active is spring.profiles.active
Hint: make sure that the external file application.properties does not exist - it has higher precedence than the application-demo.properties on the classpath

#### Solution
1. export SPRING_PROFILES_ACTIVE=demo
2. run the application with mvn spring-boot:run

or

1. run the application directly with SPRING_PROFILES_ACTIVE=demo mvn spring-boot:run
