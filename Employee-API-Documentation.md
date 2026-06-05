# Employee REST API Documentation

## Overview
This Spring Boot application exposes an Employee REST API backed by an in-memory H2 database. The implementation uses:

- JPA entity mapping (`Employee`)
- Spring Data repository (`EmployeeRepository`)
- service layer (`EmployeeService`)
- REST controller (`EmployeeController`)
- H2 initialization scripts (`schema.sql`, `data.sql`)

The API is available under `/api/employees`.

---

## 1. Entity: `Employee`
File: `src/main/java/com/example/Java/REST/API/Demo/entity/Employee.java`

### What it does
- Defines the `Employee` domain model
- Maps the class to the `employee` table in H2 using JPA annotations

### Key annotations and fields
- `@Entity`: Marks this class as a JPA entity
- `@Table(name = "employee")`: Maps the entity to the `employee` table
- `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Primary key generation
- `@Column(name = "first_name", nullable = false)`: Maps `firstName` to `first_name` column
- `@Column(nullable = false, unique = true)`: Enforces a required unique `email`

### Fields
- `id`: Long primary key
- `firstName`: Employee first name
- `lastName`: Employee last name
- `email`: Unique employee email
- `jobTitle`: Employee job title
- `salary`: Employee salary as `BigDecimal`

### Constructors and getters/setters
- Default constructor required by JPA
- Parameterized constructor used when creating new employees
- Standard getter and setter methods for all fields

---

## 2. Repository: `EmployeeRepository`
File: `src/main/java/com/example/Java/REST/API/Demo/repository/EmployeeRepository.java`

### What it does
- Extends `JpaRepository<Employee, Long>`
- Provides CRUD operations automatically for `Employee`

### Why it exists
- Spring Data JPA generates standard database operations
- No implementation code is required in this interface

---

## 3. Service: `EmployeeService`
File: `src/main/java/com/example/Java/REST/API/Demo/service/EmployeeService.java`

### What it does
- Encapsulates business logic for employee operations
- Uses `EmployeeRepository` for persistence

### Methods
- `getAllEmployees()`: Returns all employees
- `getEmployeeById(Long id)`: Fetches a single employee by ID
  - Throws a runtime exception if not found
- `createEmployee(Employee employee)`: Saves a new employee
- `updateEmployee(Long id, Employee updatedEmployee)`: Updates an existing employee
  - Loads the existing entity, updates fields, and saves it back
- `deleteEmployee(Long id)`: Deletes an employee by ID

### Why this layer exists
- Separates persistence logic from the REST controller
- Makes the code easier to maintain and test

---

## 4. Controller: `EmployeeController`
File: `src/main/java/com/example/Java/REST/API/Demo/controller/EmployeeController.java`

### What it does
- Exposes REST endpoints for managing employees
- Delegates work to `EmployeeService`

### Controller annotations
- `@RestController`: Marks the class as a REST controller
- `@RequestMapping("/api/employees")`: Base path for all employee routes

### Endpoints
- `GET /api/employees`
  - Returns a list of all employees
- `GET /api/employees/{id}`
  - Returns a specific employee by ID
- `POST /api/employees`
  - Creates a new employee
  - Accepts JSON body for employee fields
  - Returns `201 Created` with the created employee
- `PUT /api/employees/{id}`
  - Updates an existing employee by ID
  - Accepts JSON body with changes
- `DELETE /api/employees/{id}`
  - Deletes the employee with the specified ID
  - Returns `204 No Content`

### Request/response handling
- `@RequestBody`: Binds request JSON to the `Employee` object
- `@PathVariable`: Binds URL path segment to method parameter
- `ResponseEntity`: Returns HTTP status and response body for `POST` and `DELETE`

---

## 5. H2 Database initialization
Files:
- `src/main/resources/schema.sql`
- `src/main/resources/data.sql`

### `schema.sql`
Creates the `employee` table with columns:
- `id` as identity primary key
- `first_name` and `last_name` as required text
- `email` as required unique text
- `job_title` as optional text
- `salary` as required decimal

### `data.sql`
Inserts initial rows into `employee`:
- Alice Patel
- Bob Sharma
- Charlie Rao

---

## 6. Application configuration
File: `src/main/resources/application.properties`

### What is configured
- `spring.datasource.url`: Uses H2 in-memory database `jdbc:h2:mem:testdb`
- `spring.datasource.driver-class-name`: H2 JDBC driver
- `spring.jpa.hibernate.ddl-auto=none`: Disable auto schema creation by Hibernate
- `spring.sql.init.mode=always`: Always run SQL init scripts
- `spring.sql.init.schema-locations=classpath:schema.sql`: Load schema creation script
- `spring.sql.init.data-locations=classpath:data.sql`: Load initial data script
- `spring.h2.console.enabled=true`: Enables the H2 console UI
- `spring.h2.console.path=/h2-console`: Console path
- `spring.jpa.open-in-view=false`: Disable open-in-view for cleaner JPA behavior

---

## 7. Execution flow step-by-step
1. Spring Boot starts the application in `JavaRestApiDemoApplication`
2. JPA entity scanning finds `Employee`
3. Spring Data registers `EmployeeRepository`
4. H2 database is initialized with `schema.sql` and `data.sql`
5. `EmployeeController` is exposed at `/api/employees`
6. HTTP requests are routed to controller methods
7. Controller methods call `EmployeeService`
8. Service methods use `EmployeeRepository` for CRUD operations
9. The app returns JSON responses to the client

---

## 8. Example API usage
- List employees:
  - `GET http://localhost:9097/api/employees`
- Get employee by ID:
  - `GET http://localhost:9097/api/employees/1`
- Create employee:
  - `POST http://localhost:9097/api/employees`
  - Body example:
    ```json
    {
      "firstName": "Dina",
      "lastName": "Kumar",
      "email": "dina.kumar@example.com",
      "jobTitle": "Designer",
      "salary": 68000.00
    }
    ```
- Update employee:
  - `PUT http://localhost:9097/api/employees/1`
- Delete employee:
  - `DELETE http://localhost:9097/api/employees/1`

---

## 9. Notes
- If you restart the app, H2 is fresh in memory and the initial `data.sql` rows are loaded again.
- If you want data to persist across restarts, switch from `jdbc:h2:mem:testdb` to a file-based H2 URL.
