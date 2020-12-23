# API

### Use Case: URL Shortener
We have been tasked with producing a URL shortener service.

A URL shortener basically converts a long and cumbersome URL to a short, human-readable token which is easier to remember.

Examples on the internet are **bit.ly** and **TinyUrl**.

Examples: 

http://localhost/company-spreadsheet -> https://docs.google.com/document/d/VOLbjBNXbT5Z3M8oDsMVhDZnkHY2oDeDwLp8cssnN8/edit#

This usecase is about making a URL Shortener service that satisfies the following document: https://documenter.getpostman.com/view/7586248/TVsvgmZS

In this document you can see what is expected of your API.


#### Concepts
- token: the unique, human-readable/memorable text that points to some URL, must not be the word "token", must be unique.
- targetUrl: the url a token is associated with. Must be a valid URI, must not be a URL on the Url Shortener service it self.
- protectToken: an api-token that is associated with a token and must be provided as a http header in order to resolve a token.
- user: the owner and controller of the token.
- token-collision: when a token is being registered more than once.
- host: the server running the url-shortener service. This is probably http://localhost
- redirect: the technique to make the browser go to a different site than written in the URL.


#### Features
Token actions:
- All actions on tokens must be authenticated.
- Register token (Prevent token-collisions).
- Test target URL. (is the target browser-redirectable, is it called "token")
- Delete token.
- Update URL by token.
- A token is owned and controlled by a user.
- Protect token (make url-shortening accessible to authenticated users)
- Circle detection (Prevent loops when tokens points to the URL shortener it self)

User actions:
- Create user (onboard a new user with username/password. Username is unique)
- Delete user (also delete all user tokens)

Follow actions:
- The all important feature of the service is retrieving a redirect from a token




### Step 1: Create Springboot web-project

### Step 2: Create a User service for handling Users
- Create a model `User` representing a User with username and password.
- Create a Service `UserService` with the following methods:
    - `User create(String userName, String password)`
    - `void delete(User user)`


### Step 3: Create a Token service for handling Tokens
- Create a model `Token` representing a Token with token, targetUrl and User.
- Create a model `ProtectToken` representing a ProtectToken with token-reference and a protectToken.
- Create a Service `TokenService` with the following methods:
    - `Token create(String token, String targetUrl, User user)`
        - collision detection
        - targetUrl validation (legal URL? Circle?)
    - `Token update(String token, String targetUrl, User user)`
        - collision detection
        - targetUrl validation (legal URL? Circle?)
        - ownership check
    - `Token delete(String token, User user)`
        - ownership check
    - `Token protect(String token, User user, String protectToken)`
        - ownership check
    - `String follow(String token)`
        - protected check
    - `String follow(String token, String protectToken)`
        - protected check
    
The TokenService uses an in-memory datastore (a HashMap).
No Token can be named "token" as it would conflict with the redirection/follow semantics. 

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