package com.example.Java.REST.API.Demo.service;

import com.example.Java.REST.API.Demo.entity.Employee;
import com.example.Java.REST.API.Demo.exception.ResourceNotFoundException;
import com.example.Java.REST.API.Demo.repository.EmployeeRepository;
import com.example.Java.REST.API.Demo.util.EmployeeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for employee business operations.
 *
 * This class handles application logic for managing employees and delegates
 * persistence operations to the repository layer.
 */
@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Read-only method: fetch all employees.
     * Uses Stream API to return a list from the repository result.
     */
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * Read-only method: fetch one employee by ID.
     * Throws ResourceNotFoundException if the employee does not exist.
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id));
    }

    /**
     * Create a new employee record.
     * This is a write operation and requires a transactional context.
     */
    @Transactional
    public Employee createEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        logger.info("Creating employee: {} {}", employee.getFirstName(), employee.getLastName());
        return employeeRepository.save(employee);
    }

    /**
     * Update an existing employee.
     * Only non-null fields in the request are copied over.
     */
    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existing = getEmployeeById(id);

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

        logger.info("Updating employee with id={}", id);
        return employeeRepository.save(existing);
    }

    /**
     * Delete an employee by ID.
     */
    @Transactional
    public void deleteEmployee(Long id) {
        Employee existing = getEmployeeById(id);
        logger.info("Deleting employee with id={}", id);
        employeeRepository.delete(existing);
    }

    /**
     * Increment all employee salaries by 10% and save the updated list.
     */
    @Transactional
    public void incrementSalaries() {
        List<Employee> employees = employeeRepository.findAll();

        List<Employee> incrementedEmployees = employees.stream()
                .map(employee -> {
                    BigDecimal increment = employee.getSalary()
                            .multiply(new BigDecimal("0.10"));
                    employee.setSalary(employee.getSalary().add(increment));
                    return employee;
                })
                .collect(Collectors.toList());

        logger.info("Incremented salaries for {} employees", incrementedEmployees.size());
        employeeRepository.saveAll(incrementedEmployees);
    }

    /**
     * Apply a custom processor function to all employees and save results.
     * Demonstrates functional interface usage and lambda expression handling.
     */
    @Transactional
    public List<Employee> processEmployees(EmployeeProcessor processor) {
        List<Employee> employees = employeeRepository.findAll()
                .stream()
                .map(processor::process)
                .collect(Collectors.toList());

        employeeRepository.saveAll(employees);
        return employees;
    }

    /**
     * Read-only filter operation using Stream API.
     */
    @Transactional(readOnly = true)
    public List<Employee> findEmployeesByJobTitle(String jobTitle) {
        return employeeRepository.findAll()
                .stream()
                .filter(emp -> emp.getJobTitle() != null &&
                        emp.getJobTitle().equalsIgnoreCase(jobTitle))
                .collect(Collectors.toList());
    }

    /**
     * Read-only filter operation for salary threshold.
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesWithSalaryAbove(BigDecimal threshold) {
        return employeeRepository.findAll()
                .stream()
                .filter(emp -> emp.getSalary().compareTo(threshold) > 0)
                .collect(Collectors.toList());
    }

    /**
     * Read-only calculation of the average salary.
     */
    @Transactional(readOnly = true)
    public BigDecimal getAverageSalary() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalSalary = employees.stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalSalary.divide(BigDecimal.valueOf(employees.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * Read-only sort operation returning employees by salary.
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesSortedBySalary() {
        return employeeRepository.findAll()
                .stream()
                .sorted((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                .collect(Collectors.toList());
    }

    /**
     * Read-only count operation. Uses repository count directly.
     */
    @Transactional(readOnly = true)
    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }
}

