# REST API Flow and Java Application Basics

## 1. What is REST?
REST stands for Representational State Transfer.
It is an architectural style for building web services that use HTTP methods to perform operations on resources.

Key REST principles:
- Resources are identified by URLs (endpoints)
- Use standard HTTP methods: GET, POST, PUT, DELETE, PATCH
- Use stateless communication: each request contains all needed information
- Use standard HTTP response codes to indicate success or failure
- Transfer resource representations, usually JSON

## 2. REST API Flow in a Java Spring Boot Application
A typical request flow in a Spring Boot REST API looks like this:

1. Client sends an HTTP request to a controller endpoint.
2. Spring matches the request URL and method to a controller method.
3. Controller calls a service layer to perform business logic.
4. Service layer interacts with the repository layer to access the database.
5. The repository returns data to the service.
6. Service returns data or a result to the controller.
7. Controller builds an HTTP response and sends it back to the client.

Example flow:
- Client: `GET /api/employees`  
- Controller: `EmployeeController.getAllEmployees()`  
- Service: `EmployeeService.getAllEmployees()`  
- Repository: `EmployeeRepository.findAll()`  
- Database: returns employee list  
- Controller sends HTTP 200 OK with JSON list

## 3. Java Application Layers in a REST API
### Controller
- Handles HTTP requests and maps them to Java methods.
- Receives input from URL path variables, query parameters, or request body.
- Returns HTTP responses with status codes and data.
- Example: `@RestController`, `@GetMapping`, `@PostMapping`.

### Service
- Contains business logic and rules.
- Coordinates actions between controllers and repositories.
- Keeps controller code clean and focused on HTTP handling.
- Example: validation, update rules, salary calculations.

### Repository
- Handles data access and database operations.
- In Spring Data JPA, repositories often extend `JpaRepository`.
- Example: `findAll()`, `findById()`, `save()`, `deleteById()`.

### Entity
- Represents database tables as Java classes.
- Uses JPA annotations like `@Entity`, `@Id`, `@Column`.
- Example: `Employee` with fields such as `id`, `name`, `jobTitle`, `salary`.

## 4. Important HTTP Terminology
### HTTP Method
- `GET`: Read a resource or list of resources.
- `POST`: Create a new resource.
- `PUT`: Update an existing resource.
- `DELETE`: Remove a resource.
- `PATCH`: Partially update a resource.

### HTTP Status Codes
- `200 OK`: Request succeeded and returned data.
- `201 Created`: Resource was successfully created.
- `204 No Content`: Request succeeded but no content is returned.
- `400 Bad Request`: Client request is invalid or malformed.
- `404 Not Found`: Resource not found.
- `500 Internal Server Error`: Server-side error occurred.

### Endpoint
- A URL path exposed by the API.
- Example: `/api/employees`, `/api/employees/{id}`, `/api/employees/filter`.

### Request Body
- Data sent by the client in POST or PUT requests.
- Often JSON in a REST API.
- Example: `{ "name": "Alice", "jobTitle": "Developer" }`.

### Response Body
- Data returned by the server.
- Usually JSON with resource fields or a success message.

## 5. Java Language Concepts Used in REST APIs
### Collection Framework
- Part of `java.util` that includes collections like `List`, `Set`, `Map`.
- Commonly used to hold and return multiple resources.
- Example: `List<Employee>` from `getAllEmployees()`.

### Functional Interface
- An interface with exactly one abstract method.
- Used with lambda expressions and method references.
- Example: `EmployeeProcessor` with a method like `Employee process(Employee employee)`.

### Lambda Expression
- A concise way to implement a functional interface.
- Syntax: `(params) -> expression` or `(params) -> { statements }`.
- Example: `employee -> employee.getSalary().multiply(new BigDecimal("0.05"))`.

### Stream API
- Introduced in Java 8 for processing collections.
- Allows operations like `filter`, `map`, `sorted`, `collect`, `count`.
- Works well for business logic such as filtering employees or computing averages.

Example stream usage:
- `employees.stream().filter(e -> e.getSalary().compareTo(threshold) > 0).collect(Collectors.toList())`
- `employees.stream().map(Employee::getSalary).reduce(BigDecimal.ZERO, BigDecimal::add)`

## 6. Exception Handling and Controller Advice
### Exception Handling
- When an error occurs, throw a Java exception.
- Examples: invalid input, missing resource.
- Spring can convert exceptions into HTTP responses.

### `@ControllerAdvice`
- A global exception handler for all controllers.
- Centralizes error handling in one place.
- Can map exceptions to specific HTTP status codes.

Example responsibilities:
- Catch `ResourceNotFoundException` and return `404 Not Found`
- Catch `IllegalArgumentException` and return `400 Bad Request`
- Catch generic exceptions and return `500 Internal Server Error`

## 7. How a Java REST API Works Together
1. The client sends a request to an endpoint.
2. The controller receives it and extracts any required data.
3. The controller calls the service layer.
4. The service applies business rules and uses the repository for persistence.
5. The repository interacts with the database and returns results.
6. The response is built and sent back to the client.
7. If any exception occurs, `@ControllerAdvice` catches it and returns a proper HTTP status.

## 8. Practical Example
For `GET /api/employees`:
- Controller: `getAllEmployees()` returns `ResponseEntity<List<Employee>>`
- Service: `employeeService.getAllEmployees()` returns a `List<Employee>`
- Repository: `employeeRepository.findAll()` returns all employees from DB
- HTTP response: `200 OK` with JSON list

For `DELETE /api/employees/{id}`:
- Controller: `deleteEmployee(id)` calls service delete method
- Service: `employeeService.deleteEmployee(id)` removes the entity
- HTTP response: `204 No Content` if deleted successfully

## 9. Summary of Key Terms
- REST: API design style using HTTP and resources
- Endpoint: API URL path
- Controller: handles HTTP requests
- Service: contains business logic
- Repository: data access layer
- Entity: database-mapped Java object
- HTTP method: GET, POST, PUT, DELETE
- Status code: numeric response indicator
- Collection Framework: `List`, `Map`, `Set`
- Stream API: process collections with functional operations
- Functional Interface: single-method interface used by lambdas
- Lambda Expression: compact function implementation
- Controller Advice: centralized exception handling

## 10. Useful Tips
- Keep controllers thin; put real logic in services.
- Use `ResponseEntity` to control status codes and response bodies.
- Use `@ControllerAdvice` to avoid repeating exception-handling code.
- Use streams for collection transformations and calculations.
- Use meaningful HTTP status codes to help API consumers.
