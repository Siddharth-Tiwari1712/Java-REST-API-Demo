package com.example.Java.REST.API.Demo.service;

import com.example.Java.REST.API.Demo.entity.Employee;
import com.example.Java.REST.API.Demo.exception.ResourceNotFoundException;
import com.example.Java.REST.API.Demo.repository.EmployeeRepository;
import com.example.Java.REST.API.Demo.util.EmployeeProcessor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE LAYER - Uses Stream API and Lambda Expressions
 * 
 * STREAM API: A functional programming approach to process collections.
 * Streams are lazy, chainable, and provide a declarative way to work with data.
 * 
 * LAMBDA EXPRESSIONS: Anonymous functions that allow us to pass behavior as data.
 * Syntax: (parameters) -> body
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Get all employees using Stream API
     * - stream(): Creates a stream from the collection
     * - collect(Collectors.toList()): Collects stream elements into a list
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * Get employee by ID with custom exception handling
     * Uses Lambda expression in orElseThrow()
     */
    public Employee getEmployeeById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id));
    }

    public Employee createEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        return employeeRepository.save(employee);
    }

    /**
     * Update employee with Stream API for field processing
     * Demonstrates functional approach to data transformation
     */
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existing = getEmployeeById(id);
        
        // Lambda expressions to conditionally update fields
        if (updatedEmployee.getFirstName() != null) {
            existing.setFirstName(updatedEmployee.getFirstName());
        }
        if (updatedEmployee.getLastName() != null) {
            existing.setLastName(updatedEmployee.getLastName());
        }
        if (updatedEmployee.getEmail() != null) {
            existing.setEmail(updatedEmployee.getEmail());
        }
        if (updatedEmployee.getJobTitle() != null) {
            existing.setJobTitle(updatedEmployee.getJobTitle());
        }
        if (updatedEmployee.getSalary() != null) {
            existing.setSalary(updatedEmployee.getSalary());
        }
        
        return employeeRepository.save(existing);
    }

    public void deleteEmployee(Long id) {
        Employee existing = getEmployeeById(id);
        employeeRepository.delete(existing);
    }

    /**
     * INCREMENT SALARIES - Stream API + Lambda Expression Example
     * 
     * COLLECTION FRAMEWORK: Working with List<Employee>
     * STREAM API: Processing collections in a functional way
     * LAMBDA EXPRESSION: (employee) -> defines the operation on each employee
     */
    public void incrementSalaries() {
        List<Employee> employees = employeeRepository.findAll();
        
        // Stream API with Lambda expression - 10% salary increment
        // map() transforms each employee's salary
        List<Employee> incrementedEmployees = employees.stream()
                .map(employee -> {
                    // Lambda expression: increase salary by 10%
                    BigDecimal increment = employee.getSalary()
                            .multiply(new BigDecimal("0.10"));
                    employee.setSalary(employee.getSalary().add(increment));
                    return employee;
                })
                .collect(Collectors.toList());
        
        // Save all updated employees
        employeeRepository.saveAll(incrementedEmployees);
    }

    /**
     * FUNCTIONAL INTERFACE Example - Process employees with custom logic
     * This method demonstrates how to use the EmployeeProcessor functional interface
     */
    public List<Employee> processEmployees(EmployeeProcessor processor) {
        return employeeRepository.findAll()
                .stream()
                .map(processor::process)
                .collect(Collectors.toList());
    }

    /**
     * Find employees by job title using Stream API + Lambda
     * Filter example: Processing and filtering collections
     */
    public List<Employee> findEmployeesByJobTitle(String jobTitle) {
        return employeeRepository.findAll()
                .stream()
                .filter(emp -> emp.getJobTitle() != null && 
                              emp.getJobTitle().equalsIgnoreCase(jobTitle))
                .collect(Collectors.toList());
    }

    /**
     * Get employees with salary above a certain threshold
     * Another filter example with Stream API
     */
    public List<Employee> getEmployeesWithSalaryAbove(BigDecimal threshold) {
        return employeeRepository.findAll()
                .stream()
                .filter(emp -> emp.getSalary().compareTo(threshold) > 0)
                .collect(Collectors.toList());
    }

    /**
     * Get average salary using Stream API
     * Demonstrates reduction operation: summarizing(Collectors)
     */
    public BigDecimal getAverageSalary() {
        return employeeRepository.findAll()
                .stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(employeeRepository.findAll().size()), 
                        BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Sort employees by salary (ascending)
     * Using Stream API with sorted() + Lambda
     */
    public List<Employee> getEmployeesSortedBySalary() {
        return employeeRepository.findAll()
                .stream()
                // Lambda expression for sorting
                .sorted((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                .collect(Collectors.toList());
    }

    /**
     * Get employee count - Terminal operation example
     * Count is a terminal operation that ends the stream pipeline
     */
    public long getTotalEmployeeCount() {
        return employeeRepository.findAll()
                .stream()
                .count();
    }
            BigDecimal newSalary = employee.getSalary().multiply(BigDecimal.valueOf(1.10));
            employee.setSalary(newSalary);
        }
        employeeRepository.saveAll(employees);
    }

    //give above  mrthod with stream api increase by 10% and add bonus of 1lakh use max or min somewhere modify func as per need and tell me max salary after increment and bonus
    public void incrementSalariesWithBonus() {
        List<Employee> employees = employeeRepository.findAll();
        BigDecimal maxSalary = employees.stream()
                .map(employee -> {
                    BigDecimal newSalary = employee.getSalary().multiply(BigDecimal.valueOf(1.10)).add(BigDecimal.valueOf(100000));
                    employee.setSalary(newSalary);
                    return newSalary;
                })
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        employeeRepository.saveAll(employees);
        System.out.println("Max salary after increment and bonus: " + maxSalary);
    }

}
