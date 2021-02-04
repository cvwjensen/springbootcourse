# API

### Use Case: API for the URL Shortener
The project _url-shortener_ project contains models and services for handling Users and Tokens.

This exercise will build on top of that project and expose the services in a REST api.

We will also build a Security Interceptor for controlling access to the API. The Interceptor will extract either a 

### Step 1: Goto the url-shortener project and use maven to build and _install_ the project

#### Solution

```commandline
cd ~/springbootcourse/url-shortener
mvn clean install
```


### Step 2: Create Springboot web-project
- Use Spring Initializr to create a new project with Web. Also include Lombok if you want to use that.
- Open the projects pom.xml and add a dependency to the url-shortener project that you installed in step 1.

#### Solution
pom.xml snippet:
```xml
        <dependency>
            <groupId>dk.lundogbendsen.springbootcourse</groupId>
            <artifactId>url-shortener</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```


### Step 4: Create an API for handling Users
Create the following API:

```
GET /user
POST /user
DELETE /user/{username}
```
### Step 5: Create an API for handling Tokens
Create the following API:

```
GET /token
POST /token
PUT /token/{token}
DELETE /token/{token}
POST /token/{token}/protect
```

### Step 6: Create an API for following a token
Create the following API:

GET /{token}