# Actuator

## Exercises on Application Metadata

Create a new Springboot project and add "Web" and "Actuator" starters.


### Exercise 1:  Enable ALL endpoints

Springboot offers a lot of endpoints to be exposed through the Actuator. A few examples 

**beans, configuration, env, mappings, info**

Make the project expose ALL available endpoints.

Run the application and hit http://localhost:8080/actuator

Hint: The configuration property controlling exposure of endpoints is `management.endpoints.web.exposure.include`.

#### Solution
In application.properties add this entry:
```
management.endpoints.web.exposure.include=*
```


### Exercise 2: Inspect the properties of your application

Springboot looks after Configuration Properties for your application in a lot of different places. Through the Actuator you are able to inspect what property got resolved to what value.

Add the following entry to application.properties:
```
server.port=8080
```

Run the application and find the property using one of the exposed endpoints in the Actuator API.

Hint: you are looking for Configuration Properties.
Hint: make sure all endpoints are exposed using the solution from exercise 1.


#### Solution:
http://localhost:8080/actuator/configprops

```
...
"server-org.springframework.boot.autoconfigure.web.ServerProperties": {
  "prefix": "server",
  "properties": {
    "port": 8080
...
```


### Exercise 3: Expose properties from your own Config classes in the Actuator

Make a **Configuration Property class** called _MailConfig_ with the following properties:

```
String smtpServer;
int port = 25;
String username;
String password;
```

Find reload the application and find them in the

http://localhost:8080/actuator/configprops

Hint: A Configuration Property class must be annotated with `@Configuration` and `@ConfigurationProperties`.

#### Solution
see 101/actuator/src/main/java/dk/lundogbendsen/health/MailConfig.java



### Exercise 4: Add a META-INF/build-info.properties and see the build info in the exposed endpoint

Use maven to generate the file by adding the below snippet to the `spring-boot-maven-plugin` in the pom.xml file:

```
<executions>
    <execution>
        <goals>
            <goal>build-info</goal>
        </goals>
    </execution>
</executions>
```


#### Solution:

The plugin-snippet should look like this:
```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>build-info</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Run the `mvn clean package` command.

Find the build info at http://localhost:8080/actuator/info


### Exercise 5: Add Team information to the Info section

Add team information such as Team name, Contact email, and Team Lead to the info section.

See the info exposed through the Actuator endpoint.

#### Solution
Add the following entries to the application.properties:
```
info.team.team-name=Team Alfa-Bravo
info.team.team-lead=The Boss
info.team.contact-email=team-alfa-bravo@example.com

```

Hit the http://localhost:8080/actuator/info


## Exercises on Health

### Exercise 1: Enable the RabbitMQ build-in health indicator

Add the `spring-boot-starter-amqp` to the `pom.xml`.

Enable the rabbit health check by adding to the `application.properties`:

`management.health.rabbit.enabled=true`

Restart the application and hit the health endpoint to see system status.

http://localhost:8080/actuator/health


### Exercise 2: Create your own health check: ServiceWindowHealthCheck

Create a Health Check that reports down if we are in a service-window period. This will help the loadbalancer to stop routing traffic the your service.

Read the service window period from the `application.properties`.


#### Solution
See `101/actuator/src/main/java/dk/lundogbendsen/health/ServiceWindowHealtIndicator.java`


