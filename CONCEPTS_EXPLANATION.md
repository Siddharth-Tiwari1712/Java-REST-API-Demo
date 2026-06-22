# Java Advanced Concepts Implementation Guide

## Overview
This document explains all the advanced Java concepts implemented in the Employee REST API.

---

## 1. COLLECTION FRAMEWORK

### What is it?
The Collection Framework is a unified architecture for representing and manipulating collections (groups of objects) in Java.

### Key Components:
- **List**: Ordered collection, allows duplicates (ArrayList, LinkedList)
- **Set**: Unordered collection, no duplicates (HashSet, TreeSet)
- **Map**: Key-value pairs (HashMap, TreeMap)

### Example in Our Code:
```java
// In EmployeeService.getAllEmployees()
List<Employee> employees = employeeRepository.findAll();
// findAll() returns a List<Employee> - a Collection Framework object
```

### Benefits:
- **Flexibility**: Choose the right collection for your needs
- **Reusability**: Common interface for all collections
- **Performance**: Different implementations for different use cases

---

## 2. FUNCTIONAL INTERFACE

### What is it?
A functional interface is an interface with **exactly ONE abstract method**. It enables the use of lambda expressions and method references.

### Key Points:
- Must have exactly 1 abstract method
- Can have multiple default methods and static methods
- Annotated with `@FunctionalInterface`

### Example in Our Code:
```java
// EmployeeProcessor.java
@FunctionalInterface
public interface EmployeeProcessor {
    Employee process(Employee employee);  // Single abstract method
}
```

### Why We Use It:
```java
// In EmployeeService
public List<Employee> processEmployees(EmployeeProcessor processor) {
    return employeeRepository.findAll()
            .stream()
            .map(processor::process)  // Pass behavior as a parameter
            .collect(Collectors.toList());
}
```

### Built-in Functional Interfaces:
- `Predicate<T>`: Returns boolean (for filtering)
- `Function<T,R>`: Takes T, returns R (for transformations)
- `Consumer<T>`: Takes T, returns void (for operations)
- `Supplier<T>`: Returns T (for providing values)

---

## 3. STREAM API

### What is it?
Stream API provides a functional programming approach to process collections. It enables you to work with sequences of elements in a declarative way.

### Key Characteristics:
- **Lazy Evaluation**: Operations not executed until a terminal operation is called
- **Chainable**: Multiple operations can be chained together
- **Functional**: Encourages immutability and function composition
- **Parallel-friendly**: Can easily parallelize operations

### Stream Operations:

#### Intermediate Operations (return Stream):
```java
// FILTER: Keep only matching elements
employeeRepository.findAll().stream()
    .filter(emp -> emp.getSalary().compareTo(threshold) > 0)
    .collect(Collectors.toList());

// MAP: Transform each element
employees.stream()
    .map(emp -> emp.getFirstName())
    .collect(Collectors.toList());

// SORTED: Order elements
employees.stream()
    .sorted((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
    .collect(Collectors.toList());
```

#### Terminal Operations (execute the stream):
```java
// COLLECT: Gather elements into a collection
.collect(Collectors.toList());

// COUNT: Get number of elements
employees.stream().count();

// FOREACH: Perform action on each element
employees.stream().forEach(System.out::println);

// REDUCE: Combine elements into single value
employees.stream()
    .map(Employee::getSalary)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

### Stream Pipeline Example:
```java
// Filter -> Map -> Sort -> Collect
employees.stream()
    .filter(emp -> emp.getSalary().compareTo(BigDecimal.valueOf(50000)) > 0)
    .map(emp -> emp.getFirstName())
    .sorted()
    .collect(Collectors.toList());
```

### Benefits:
- **Readable**: Declarative code is easier to understand
- **Concise**: Less boilerplate code
- **Parallel**: Can easily use parallel streams for large datasets
- **Composable**: Easy to chain multiple operations

---

## 4. LAMBDA EXPRESSION

### What is it?
A lambda expression is an anonymous function (function without a name) that allows you to treat functionality as a method argument.

### Syntax:
```
(parameters) -> body
```

### Examples:

#### Simple Lambda (Single parameter):
```java
// Filter employees by job title
.filter(emp -> emp.getJobTitle().equalsIgnoreCase("Developer"))
```

#### Lambda with Multiple Parameters:
```java
// Comparator for sorting
(e1, e2) -> e1.getSalary().compareTo(e2.getSalary())
```

#### Lambda with Multiple Statements:
```java
// Complex transformation
.map(employee -> {
    BigDecimal increment = employee.getSalary().multiply(new BigDecimal("0.10"));
    employee.setSalary(employee.getSalary().add(increment));
    return employee;
})
```

#### Method Reference (Shorthand for Lambda):
```java
// Instead of: .map(emp -> emp.getSalary())
// Use:
.map(Employee::getSalary)

// Instead of: .forEach(emp -> System.out.println(emp))
// Use:
.forEach(System::out::println)
```

### Benefits:
- **Concise**: Less boilerplate code than anonymous classes
- **Readable**: Intent is clear
- **Reusable**: Can pass functions as parameters

### Comparison: Before and After

**Before Lambda (Old Way):**
```java
List<Employee> highSalaryEmployees = new ArrayList<>();
for (Employee emp : employees) {
    if (emp.getSalary().compareTo(threshold) > 0) {
        highSalaryEmployees.add(emp);
    }
}
```

**After Lambda (Modern Way):**
```java
List<Employee> highSalaryEmployees = employees.stream()
    .filter(emp -> emp.getSalary().compareTo(threshold) > 0)
    .collect(Collectors.toList());
```

---

## 5. HTTP STATUS CODES

### What are they?
HTTP status codes are standardized codes that indicate the outcome of an HTTP request.

### Common Status Codes:

#### Success (2xx):
```
200 OK
- Request succeeded
- Return: Full response body
- Example: GET /api/employees ✓

201 CREATED
- Resource successfully created
- Return: Created resource with Location header
- Example: POST /api/employees ✓

204 NO_CONTENT
- Request succeeded but no content to return
- Return: Empty body
- Example: DELETE /api/employees/{id} ✓
```

#### Client Errors (4xx):
```
400 BAD_REQUEST
- Invalid input or malformed request
- Example: POST with invalid JSON

404 NOT_FOUND
- Resource doesn't exist
- Example: GET /api/employees/999 (if 999 doesn't exist)

409 CONFLICT
- Request conflicts with current state
- Example: Duplicate email on create
```

#### Server Errors (5xx):
```
500 INTERNAL_SERVER_ERROR
- Server encountered an error
- Example: Database connection failure
```

### In Our Code:
```java
// 200 OK - GET request
@GetMapping("/{id}")
public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    Employee employee = employeeService.getEmployeeById(id);
    return new ResponseEntity<>(employee, HttpStatus.OK);
}

// 201 CREATED - POST request
@PostMapping
public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
    Employee created = employeeService.createEmployee(employee);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
}

// 204 NO_CONTENT - DELETE request
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
}
```

---

## 6. CONTROLLER ADVICE - EXCEPTIONAL HANDLER

### What is it?
`@ControllerAdvice` is a Spring annotation that allows you to handle exceptions globally across the entire application.

### Key Benefits:
- **Centralized Error Handling**: One place to manage all exceptions
- **Consistency**: Same error response format everywhere
- **Reduced Code Duplication**: No need to handle exceptions in every controller
- **Maintainability**: Easy to modify error handling logic

### Architecture:

```
Request
   ↓
Controller (throws exception)
   ↓
@ControllerAdvice (catches it)
   ↓
@ExceptionHandler (handles specific exception)
   ↓
ErrorResponse (returns formatted error)
```

### Exception Handling Flow:

#### 1. Custom Exception:
```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

#### 2. Service throws exception:
```java
public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Employee not found with id: " + id));
}
```

#### 3. Global Exception Handler:
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
```

#### 4. Error Response Structure:
```json
{
    "status": 404,
    "message": "Employee not found with id: 999",
    "timestamp": "2026-06-23T10:30:00",
    "path": "/api/employees/999"
}
```

### Multiple Exception Handlers:
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    // Handle ResourceNotFoundException (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(...) { ... }
    
    // Handle IllegalArgumentException (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(...) { ... }
    
    // Handle all other exceptions (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(...) { ... }
}
```

---

## How It All Works Together

### Example 1: Get Employee with Error Handling
```
Client: GET /api/employees/999
            ↓
Controller: Calls employeeService.getEmployeeById(999)
            ↓
Service: Throws ResourceNotFoundException
            ↓
@ControllerAdvice: Catches the exception
            ↓
@ExceptionHandler: Formats error response
            ↓
Response: {
    "status": 404,
    "message": "Employee not found with id: 999",
    "timestamp": "2026-06-23T10:30:00",
    "path": "/api/employees/999"
}
```

### Example 2: Stream API + Lambda
```
Client: GET /api/employees/filter?jobTitle=Developer
            ↓
Controller: Calls service.findEmployeesByJobTitle("Developer")
            ↓
Service uses Stream API + Lambda:
employees.stream()
    .filter(emp -> emp.getJobTitle()
            .equalsIgnoreCase("Developer"))  // Lambda expression
    .collect(Collectors.toList());  // Terminal operation
            ↓
Response: [Employee{id:1, jobTitle:"Developer"}, ...]
```

---

## Testing These Endpoints

### Using cURL:

```bash
# 1. Get all employees
curl http://localhost:8080/api/employees

# 2. Get employee by ID (success - 200)
curl http://localhost:8080/api/employees/1

# 3. Get employee by ID (not found - 404, handled by @ControllerAdvice)
curl http://localhost:8080/api/employees/999

# 4. Create employee (201)
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john@example.com","jobTitle":"Developer","salary":75000}'

# 5. Update employee (200)
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","email":"jane@example.com","jobTitle":"Senior Developer","salary":85000}'

# 6. Delete employee (204)
curl -X DELETE http://localhost:8080/api/employees/1

# 7. Get average salary (Stream API)
curl http://localhost:8080/api/employees/salary/average

# 8. Get employees by job title (Stream + Lambda)
curl "http://localhost:8080/api/employees/filter?jobTitle=Developer"

# 9. Get employees above salary threshold (Stream)
curl "http://localhost:8080/api/employees/salary-threshold?amount=50000"

# 10. Get employees sorted by salary (Stream + Lambda)
curl http://localhost:8080/api/employees/sorted/salary

# 11. Increment all salaries (Stream + Lambda)
curl -X POST http://localhost:8080/api/employees/salary/increment

# 12. Get total employee count (Stream Terminal Operation)
curl http://localhost:8080/api/employees/count
```

---

## Summary

| Concept | Purpose | Example |
|---------|---------|---------|
| **Collection Framework** | Store and manage groups of objects | List<Employee> |
| **Functional Interface** | Define contracts with single abstract method | EmployeeProcessor |
| **Stream API** | Process collections functionally | .stream().filter().map().collect() |
| **Lambda Expression** | Write anonymous functions concisely | emp -> emp.getSalary() > 50000 |
| **HTTP Status Codes** | Indicate HTTP request outcomes | 200, 201, 204, 400, 404, 500 |
| **@ControllerAdvice** | Handle exceptions globally | GlobalExceptionHandler |

---

## Key Takeaways

1. **Streams** make collections processing more readable and efficient
2. **Lambdas** reduce boilerplate and make code more expressive
3. **Functional Interfaces** enable functional programming patterns
4. **Proper HTTP Status Codes** improve API usability
5. **@ControllerAdvice** creates consistent error handling across the application
6. **Collection Framework** provides flexible and powerful data structures
