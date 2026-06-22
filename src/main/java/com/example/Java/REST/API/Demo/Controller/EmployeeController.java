package com.example.Java.REST.API.Demo.controller;

import com.example.Java.REST.API.Demo.entity.Employee;
import com.example.Java.REST.API.Demo.service.EmployeeService;
import com.example.Java.REST.API.Demo.util.EmployeeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * EMPLOYEE CONTROLLER
 * 
 * HTTP STATUS CODES Used:
 * - 200 OK: Request succeeded
 * - 201 CREATED: Resource successfully created
 * - 204 NO_CONTENT: Successful request with no content to return
 * - 400 BAD_REQUEST: Invalid input or bad request
 * - 404 NOT_FOUND: Resource not found
 * - 500 INTERNAL_SERVER_ERROR: Server error (handled by @ControllerAdvice)
 * 
 * Exception handling is now managed by @ControllerAdvice (GlobalExceptionHandler)
 * This reduces code duplication and centralizes error handling
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Constructor injection for better testability and immutability
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET all employees
     * HTTP Status: 200 OK
     * Collection Framework: Returns List<Employee>
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * GET employee by ID
     * HTTP Status: 200 OK (success) or 404 NOT_FOUND (via @ControllerAdvice)
     * Exception handling: ResourceNotFoundException is caught by GlobalExceptionHandler
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    /**
     * POST - Create new employee
     * HTTP Status: 201 CREATED (indicates new resource was created)
     * Stream API: Service processes the employee creation
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    /**
     * PUT - Update existing employee
     * HTTP Status: 200 OK (successful update)
     * Lambda Expression: Service uses lambda to conditionally update fields
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    /**
     * DELETE - Remove employee
     * HTTP Status: 204 NO_CONTENT (successful deletion, no content to return)
     * This is a better practice than 200 OK for DELETE operations
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * SALARY OPERATIONS - Demonstrates Stream API and Lambda
     */
    
    /**
     * POST - Increment all employees' salaries by 10%
     * HTTP Status: 200 OK
     * Stream API: Service uses streams to process all employees
     */
    @PostMapping("/salary/increment")
    public ResponseEntity<Map<String, String>> incrementSalaries() {
        employeeService.incrementSalaries();
        return ResponseEntity.ok(Map.of("message", "All salaries incremented by 10%"));
    }

    /**
     * GET - Get average salary of all employees
     * HTTP Status: 200 OK
     * Stream API: Demonstrates reduction operation
     */
    @GetMapping("/salary/average")
    public ResponseEntity<BigDecimal> getAverageSalary() {
        BigDecimal averageSalary = employeeService.getAverageSalary();
        return new ResponseEntity<>(averageSalary, HttpStatus.OK);
    }

    /**
     * GET - Get total employee count
     * HTTP Status: 200 OK
     * Stream API Terminal Operation: count()
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getEmployeeCount() {
        long count = employeeService.getTotalEmployeeCount();
        return ResponseEntity.ok(Map.of("totalEmployees", count));
    }

    /**
     * GET - Get employees sorted by salary
     * HTTP Status: 200 OK
     * Stream API: sorted() + Lambda expression for comparator
     */
    @GetMapping("/sorted/salary")
    public ResponseEntity<List<Employee>> getEmployeesSortedBySalary() {
        List<Employee> employees = employeeService.getEmployeesSortedBySalary();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * GET - Filter employees by job title
     * HTTP Status: 200 OK
     * Stream API: filter() + Lambda expression
     * Query Parameter: /api/employees/filter?jobTitle=Developer
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Employee>> filterByJobTitle(
            @RequestParam String jobTitle) {
        List<Employee> employees = employeeService.findEmployeesByJobTitle(jobTitle);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * GET - Get employees with salary above threshold
     * HTTP Status: 200 OK
     * Stream API: filter() operation
     * Query Parameter: /api/employees/salary-threshold?amount=50000
     */
    @GetMapping("/salary-threshold")
    public ResponseEntity<List<Employee>> getEmployeesAboveSalary(
            @RequestParam BigDecimal amount) {
        List<Employee> employees = employeeService.getEmployeesWithSalaryAbove(amount);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * POST - Apply custom processor using Functional Interface
     * HTTP Status: 200 OK
     * Demonstrates: Functional Interface, Lambda Expression
     * 
     * Example usage: Send operation as query parameter
     * /api/employees/process?operation=INCREMENT_SALARY
     */
    @PostMapping("/process")
    public ResponseEntity<List<Employee>> processEmployees(
            @RequestParam(required = false) String operation) {
        
        // Using Functional Interface with Lambda Expression
        // Lambda expression defines the processing logic
        EmployeeProcessor processor = employee -> {
            if ("INCREMENT_SALARY".equals(operation)) {
                BigDecimal increment = employee.getSalary()
                        .multiply(new BigDecimal("0.05"));
                employee.setSalary(employee.getSalary().add(increment));
            }
            return employee;
        };
        
        List<Employee> processedEmployees = employeeService.processEmployees(processor);
        return new ResponseEntity<>(processedEmployees, HttpStatus.OK);
    }
}
