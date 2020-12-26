# API

## Exercises - Section 1: Controlling input and output

### Exercise 1: Create a RestController
- Create a Springboot Web project
- Create a class MyRestController
- Annotate the class with @RestController. This instructs Spring to register the class as a request handler.
- Create a method `public String helloWorld()` returning a String with content "HelloWorld".
- Annotate the method with @GetMapping. This instructs Spring that this controller handles GET requests without a path and without data.
- Run the application.
- Go to http://localhost:8080

#### Solution
```
@RestController
public class MyRestController {
    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }
}
```

### Exercise 2: Input to controller
You can map a method parameter to either a query parameter or a form field using the @RequestParam annotation.
- Modify the helloWorld() method by adding a String message as parameter.
- Modify the hard coded return value to `"Hello world: " + message`.
- The new parameter must be annotated with @RequestParam. This instructs Spring to map a query parameter from the request to this parameter. The query parameter must be called "message".
- Restart the application.
- Go to http://localhost:8080?message=alive!

#### Solution
```
@RestController
public class MyRestController {
    @GetMapping
    public String helloWorld(@RequestParam String message) {
        return "Hello World: " + message;
    }
}
```

### Exercise 3: Input to controller by path
You can map a method parameter to some part of the request path using the @PathVariable annotation.
- Make a method `public Long pathMapping(Long id)`.
- Let it return the id;
- Annotate the method with `@GetMapping("{id}")`.
- Annotate the id parameter with `@PathVariable`.
- Restart the application.
- Go to http://localhost:8080/666

#### Solution
```
@GetMapping("{id}")
public Long pathMapping(@PathVariable Long id) {
    return id;
}
```

### Exercise 4: Map input to POJO
You can have Springboot convert the request body to a Pojo. For instance is the client is sending Json, you could make a Model representing the Json and Springboot will make a Pojo with the input.
- Make a class Person with fields firstName and lastName. Add setters and getters (Hint: Lombok).
- Make a method `public void receivePerson(Person person)`.
- Annotate the method with @PostMapping to instruct Spring to map the POST verb.
- Annotate the person parameter with @RequestBody to instruct Spring to convert the request body to a Person object
- Restart the app.
- Run `curl -H "content-type: application/json" localhost:8080 -d '{"firstName":"Christian","lastName":"Jensen"}'`.

#### Solution
```
@PostMapping
public void receivePerson(@RequestBody Person person) {
    System.out.println("person = " + person);
}
```

### Exercise 5: Upload file
A file upload is really just a http request where the content part should be interpretated as a file. But we have to receive it as a MultipartFile object. Let's try to receive an upload:
- Make a method `public String receiveFile(@RequestParam("file") MultipartFile file)`
- Annotate the method with @PostMapping("/file") to instruct Spring to map the method to the POST verb and the path /file.
- In the method take out the bytes from the file, convert them to a new String() and return that String.
- Restart the application
- Run `curl localhost:8080/file -F "file=@docker-compose.yml"` (or some other local text-file)

#### Solution
```
@PostMapping("/file")
public String receiveFile(@RequestParam("file") MultipartFile file) throws IOException {
    final String content = new String(file.getBytes());
    System.out.println("content = " + content);
    return file.getOriginalFilename();
}
```


### Exercise 5: Get Header
You can map any header to your method by using the @RequestHeader annotation.
- Modify the receiveFile method by adding the parameter `@RequestHeader(name = "Content-Type") String contentType`.
- Print out the content type to the console.
- Restart the application.
- Run `curl localhost:8080/file -F "file=@docker-compose.yml"` (or some other local text-file)

#### Solution
```
@PostMapping("/file")
public String receiveFile(@RequestParam("file") MultipartFile file, @RequestHeader(name = "Content-Type") String contentType) throws IOException {
    final String content = new String(file.getBytes());
    System.out.println("content = " + content);
    System.out.println("contentType = " + contentType);
    return file.getOriginalFilename();
}
```

### Exercise 6: Pojo output
It is easy to send Strings and primitives like longs and ints. Just let the controller method return them, and they are automatically converted to the response body.
But it is just as easy to send a complex object like a pojo.
- Make a method `public Person getPerson()` that creates a Person object and returns it.
- Annotate the method with `@GetMapping("person")`
- Restart the application.
- Run `curl localhost:8080/person`.

#### Solution:
```
@GetMapping("person")
public Person getPerson() {
    Person p = new Person();
    p.setFirstName("Christian");
    p.setLastName("Jensen");
    return p;
}
```

### Exercise 7: Complete control using ResponseEntity
Sometimes you want to control the response headers in the Controller. In this case you can make the method return a ResponseEntity. 
The ResponseEntity represents both http status code, headers and content in one object.

- Make a method `public ResponseEntity<String> getPersonSpecial()`
- Annotate with `@GetMapping("person-special")`
- Make a Person object.
- Make a new ResponseEntity object for a Person object.
- Add a header "TraceId" with some random value. A practical application for this TraceId would be for the client to have a concrete reference to the the request in case something goes wrong and the client wants to call customer support.
- Add a status code "OK".
- Add the person object.
- Return the ResponseEntity.
- Restart the application.
- Run `curl localhost:8080/person-special -i` and inspect the result.

#### Solution
```
@GetMapping("person-special")
public ResponseEntity<Person> getPersonSpecial() {
    final Person person = new Person();
    person.setFirstName("Christian");
    person.setLastName("Jensen");
    HttpHeaders headers = new HttpHeaders();
    headers.add("TraceId", "abc123");
    final ResponseEntity<Person> ok = new ResponseEntity<>(person, headers, HttpStatus.OK);
    return ok;
}
```

### Exercise 8: Download a File
You can use the ResponseEntity to serve files to the client.
- Make a method `public ResponseEntity<Resource> download(String param) throws IOException`.
- Annotate with `@GetMapping(path = "/download")`
- Create a File object pointing to an actual file on your harddrive.
- Create an InputStreamResource to wrap the file like: `InputStreamResource resource = new InputStreamResource(new FileInputStream(file))`.
- Create a ResponseEntity of type InputStreamResource where you set:
  - content-length (length of file)
  - content-type (MediaType.APPLICATION_OCTET_STREAM)
  - content-disposition (attachment; filename=<the files name>)
  - body (the InputStreamResource you created)
- Restart the application.
- Go to localhost:8080/download in a browser

#### Solution
```
    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(String param) throws IOException {
        File file = new File("api/http-status-codes.png");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=http-status-codes.png")
                .body(resource);
    }
```

### Exercise 9: Client wants XML
In http there is a concept of Content Negotiation. The client sends a header "Accept" which declares which formats it can consume. 
The DispatcherServlet will then try to convert the result of the controller to the accepted format.
- In order to serve XML, a new dependency must be added to the pom.xml:
```
<dependency>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```
- Restart the Application.
- Run `curl localhost:8080/person -H "Accept: application/xml`.


### Exercise 10: Client sends XML
Json is the dominating paradigm of message exchange format on the web. But sometimes the client send XML.
Since you have added the XML capable converter as a dependency in the previous exercise, you can now receive XML as well: 
- Run `curl -H "content-type: application/xml" localhost:8080 -d '<Person><firstName>Christian</firstName><lastName>Jensen</lastName></Person>'`

## Exercises - Section 2: Exception Handling
When exceptions happens, Springboot gives you some basic handling out of the box. If errors are client side, it has a variety of handlers that produce meaningful error reponses for that. All other errors result in a 500 error.

As always Springboot lets you take complete control by setting up some error handlers. You can control exceptions directly in the controller using the @ExceptionHandler annotation, or you can create a @ControllerAdvice that handles exceptions across many controllers.

The latter approach is recommended.



### Exercise 1: Create a RestController
