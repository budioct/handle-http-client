# HTTP Client Demonstration Project

This project demonstrates how to perform Remote Procedure Calls (RPC) to REST APIs using three different HTTP client approaches in Spring Boot:
1. RestTemplate (Synchronous)
2. WebClient (Reactive)
3. OpenFeign (Declarative)

The application connects to the public JSON Placeholder API (https://jsonplaceholder.typicode.com) to fetch todo items.

## Technologies Used

- Java 21
- Spring Boot 3.4.3
- Spring WebFlux
- Spring Cloud OpenFeign
- Project Lombok
- Maven

## Project Structure

The project is organized into the following components:

- **Controller**: Handles HTTP requests and delegates to the appropriate service
- **Services**: Implements the three different HTTP client approaches
- **DTO**: Data Transfer Object representing the todo item
- **Exception Handling**: Custom exceptions and global exception handler
- **Configuration**: Configuration for the HTTP clients

## Setup Instructions

1. Clone the repository
2. Make sure you have Java 21 installed
3. Build the project using Maven:
   ```
   mvn clean install
   ```
4. Run the application:
   ```
   mvn spring-boot:run
   ```
   or
   ```
   java -jar target/latihan-client-0.0.1-SNAPSHOT.jar
   ```

## API Endpoints

The application exposes the following endpoints:

### RestTemplate Endpoints

- `GET /todos/resttemplate` - Get all todos using RestTemplate
- `GET /todos/resttemplate/{id}` - Get a specific todo by ID using RestTemplate

### WebClient Endpoints

- `GET /todos/webclient` - Get all todos using WebClient
- `GET /todos/webclient/{id}` - Get a specific todo by ID using WebClient

### OpenFeign Endpoints

- `GET /todos/feign` - Get all todos using OpenFeign
- `GET /todos/feign/{id}` - Get a specific todo by ID using OpenFeign

## Implementation Details

### RestTemplate Implementation

RestTemplate is a synchronous HTTP client provided by Spring. It's simple to use but blocks the thread while waiting for the response.

```java
public List<TodoDTO> getTodos() {
    String url = "https://jsonplaceholder.typicode.com/todos/";
    TodoDTO[] todos = restTemplate.getForObject(url, TodoDTO[].class);
    return Arrays.asList(todos != null ? todos : new TodoDTO[0]);
}

public TodoDTO getTodoById(int id) {
    String url = "https://jsonplaceholder.typicode.com/todos/" + id;
    try {
        TodoDTO data = restTemplate.getForObject(url, TodoDTO.class);
        return data;
    } catch (HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ResourceNotFoundException("Todo with id " + id + " not found");
        }
        throw e;
    }
}
```

### WebClient Implementation

WebClient is a reactive, non-blocking HTTP client introduced in Spring 5. It provides a functional, fluent API and supports streaming scenarios.

```java
public Flux<TodoDTO> getTodos() {
    return webClient.get()
            .uri("/todos/")
            .retrieve()
            .bodyToFlux(TodoDTO.class);
}

public Mono<TodoDTO> getTodoById(int id) {
    return webClient.get()
            .uri("/todos/{id}", id)
            .retrieve()
            .onStatus(
                    status -> status.is4xxClientError(),
                    response -> {
                        if (response.statusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.error(new ResourceNotFoundException("Todo with id " + id + " not found"));
                        }
                        return Mono.error(new RuntimeException("Client error"));
                    }
            )
            .bodyToMono(TodoDTO.class);
}
```

### OpenFeign Implementation

OpenFeign is a declarative HTTP client that simplifies HTTP API calls. It uses annotations to define the API interface, and the implementation is generated at runtime.

```java
@FeignClient(name = "jsonplaceholder", url = "https://jsonplaceholder.typicode.com", configuration = FeignConfig.class)
public interface TodoFeignClient {

    @GetMapping("/todos/")
    List<TodoDTO> getTodos();

    @GetMapping("/todos/{id}")
    TodoDTO getTodoById(@PathVariable(name = "id") int id);
}
```

## Error Handling

Each HTTP client implementation includes error handling for 404 Not Found responses:

- **RestTemplate**: Uses try-catch with HttpClientErrorException
- **WebClient**: Uses the onStatus operator
- **OpenFeign**: Uses a custom ErrorDecoder

## Testing the API

You can test the API using the provided HTTP request file in the `doc-api` directory:

```
### GET Todos using RestTemplate
GET http://localhost:8080/todos/resttemplate

### GET Detail Todo using RestTemplate, max id 200
GET http://localhost:8080/todos/resttemplate/1

### GET Todos using WebClient
GET http://localhost:8080/todos/webclient

### GET Detail Todo using WebClient, max id 200
GET http://localhost:8080/todos/webclient/2

### GET Todos using Feign
GET http://localhost:8080/todos/feign

### GET Detail Todo using OpenFeign, max id 200
GET http://localhost:8080/todos/feign/2
```

## Comparison of HTTP Clients

### RestTemplate
- **Pros**: Simple to use, familiar API, synchronous programming model
- **Cons**: Blocks the thread, not suitable for high-throughput applications

### WebClient
- **Pros**: Non-blocking, reactive, supports streaming, functional API
- **Cons**: Steeper learning curve, requires understanding of reactive programming

### OpenFeign
- **Pros**: Declarative, reduces boilerplate, easy to maintain
- **Cons**: Less flexible for complex scenarios, requires additional configuration

## Conclusion

This project demonstrates three different approaches to making HTTP requests in a Spring Boot application. Each approach has its own advantages and use cases:

- Use **RestTemplate** for simple, synchronous requests where blocking is acceptable
- Use **WebClient** for non-blocking, reactive applications with high throughput requirements
- Use **OpenFeign** for declarative, maintainable client code with minimal boilerplate

Choose the appropriate client based on your application's requirements for performance, maintainability, and programming model.