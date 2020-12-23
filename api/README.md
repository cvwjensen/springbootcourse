# API

## Exercises

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
- Make a method `public Person receivePerson(Person person)`.
- Annotate the method with @PostMapping to instruct Spring to map the POST verb.
- Annotate the person parameter with @RequestBody to instruct Spring to convert the request body to a Person object
- Restart the app.
- Run `curl -H "content-type: application/json" localhost:8080 -d '{"firstName":"Christian","lastName":"Jensen"}'`.

#### Solution
```
@PostMapping
public Person receivePerson(@RequestBody Person person) {
    return person;
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

