## Exercises Config-Server

### 1: Create a github repo with public access

- add new file: application.properties with the property: `server.port=8082`
- commit and push the file.

### 2: Create a new web-project called `Config-Server`

- update the application.properties with the following content:

```properties
server.port=8888
spring.cloud.config.server.git.uri=git@github.com:<your repo>/config-server.git
```

This will make the config-server listen to port 8888, and access the github repo (eg `cvwjensen/config-server`).

- Also enable the config-server autoconfiguration by adding `@EnableConfigServer` on the main class like:

```
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

### 3: Start the config server

Now the server should be listening on port 8888 and have access to your repo.

- Test the config-server with `curl localhost:8888/application/default`

You should see a json structure containing the content of your application.properties.


 
## Exercises Client

### 1: Create new Web+Config project

Inspect the pom.xml and see that you now have a new section called `<dependencyManagement>` including `spring-cloud-dependencies`. ConfigServer belongs to Spring Cloud stuff.

Update the application.properties with the following content:

```properties
spring.application.name=<your-well-describing-name>
spring.config.import=optional:configserver
server.port=8084
```

The `name` property is how the application will identify itself to the config server, so take care to make good naming conventions for your projects in the future.

The `import` property instructs the config client that is should try to pull its configuration from a config-server. The default URI of the server is `http://localhost:8888`, 
and that is why we did not have to specify it. 

Should we want to, update the property like: 

`spring.config.import=optional:configserver:https://URI-of-your-config-server`.

Furthermore, the `optional` part indicates that it's ok not being able to pull a configuration. Then you just continue with any other configuration lying around. 

If you remove `optional`, then the application will not start if it cannot pull a configuration from the config-server.

### 2: Start the client

- What port does the client listen to? It should be port `8082`. 

So the configuration from config-server has precedence over local configuration!


### 3: Add a new property file on the repo with the name of your app. Eg: `client1.properties`.

- Specify `server.port=8085`.
- Commit/push the file to the repo.
- Restart your client-app.

Now you should see that it listens to the port you just added.

So this tells us that values from `application.properties` are default values for any client connecting to the config-server. 

But if the config-server can find a specific property file, it's values will take precedence.

### 4: Activate the `demo` profile.

- Add the following line to your local application.properties: `spring.profiles.active=demo`.
- Add a new file to the config-server-repo with the name: `<your-app-name>-demo.properties`.
- In that file you set the `server.port=8086`.
- Restart your application.

Now your application should pull the demo version of the configuration and listen on port 8086.


