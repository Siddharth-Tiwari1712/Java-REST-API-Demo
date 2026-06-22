package com.example.Java.REST.API.Demo.controller;

import com.example.Java.REST.API.Demo.dto.EmployeeMapper;
import com.example.Java.REST.API.Demo.dto.EmployeeRequestDto;
import com.example.Java.REST.API.Demo.dto.EmployeeResponseDto;
import com.example.Java.REST.API.Demo.entity.Employee;
import com.example.Java.REST.API.Demo.service.EmployeeService;
import com.example.Java.REST.API.Demo.util.EmployeeProcessor;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    // Constructor injection for better testability and immutability
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET all employees
     * HTTP Status: 200 OK
     */
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        logger.info("Request received: GET /api/employees");
        List<EmployeeResponseDto> employees = EmployeeMapper.toDtoList(employeeService.getAllEmployees());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * GET employee by ID
     * HTTP Status: 200 OK or 404 NOT_FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        logger.info("Request received: GET /api/employees/{}", id);
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(EmployeeMapper.toDto(employee), HttpStatus.OK);
    }

    /**
     * POST - Create new employee
     * HTTP Status: 201 CREATED
     */
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        logger.info("Request received: POST /api/employees");
        Employee employee = EmployeeMapper.toEntity(employeeRequestDto);
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(EmployeeMapper.toDto(createdEmployee), HttpStatus.CREATED);
    }

    /**
     * PUT - Update existing employee
     * HTTP Status: 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        logger.info("Request received: PUT /api/employees/{}", id);
        Employee employee = EmployeeMapper.toEntity(employeeRequestDto);
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return new ResponseEntity<>(EmployeeMapper.toDto(updatedEmployee), HttpStatus.OK);
    }

    /**
     * DELETE - Remove employee
     * HTTP Status: 204 NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Request received: DELETE /api/employees/{}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST - Increment all employees' salaries by 10%
     * HTTP Status: 200 OK
     */
    @PostMapping("/salary/increment")
    public ResponseEntity<Map<String, String>> incrementSalaries() {
        logger.info("Request received: POST /api/employees/salary/increment");
        employeeService.incrementSalaries();
        return ResponseEntity.ok(Map.of("message", "All salaries incremented by 10%"));
    }

    /**
     * GET - Get average salary of all employees
     * HTTP Status: 200 OK
     */
    @GetMapping("/salary/average")
    public ResponseEntity<BigDecimal> getAverageSalary() {
        logger.info("Request received: GET /api/employees/salary/average");
        BigDecimal averageSalary = employeeService.getAverageSalary();
        return new ResponseEntity<>(averageSalary, HttpStatus.OK);
    }

    /**
     * GET - Get total employee count
     * HTTP Status: 200 OK
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getEmployeeCount() {
        logger.info("Request received: GET /api/employees/count");
        long count = employeeService.getTotalEmployeeCount();
        return ResponseEntity.ok(Map.of("totalEmployees", count));
    }

    /**
     * GET - Get employees sorted by salary
     * HTTP Status: 200 OK
     */
    @GetMapping("/sorted/salary")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeesSortedBySalary() {
        logger.info("Request received: GET /api/employees/sorted/salary");
        List<EmployeeResponseDto> employees = EmployeeMapper.toDtoList(employeeService.getEmployeesSortedBySalary());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * GET - Filter employees by job title
     * HTTP Status: 200 OK
     */
    @GetMapping("/filter")
    public ResponseEntity<List<EmployeeResponseDto>> filterByJobTitle(
            @RequestParam String jobTitle) {
        logger.info("Request received: GET /api/employees/filter?jobTitle={}", jobTitle);
        List<EmployeeResponseDto> employees = EmployeeMapper.toDtoList(employeeService.findEmployeesByJobTitle(jobTitle));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * GET - Get employees with salary above threshold
     * HTTP Status: 200 OK
     */
    @GetMapping("/salary-threshold")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeesAboveSalary(
            @RequestParam BigDecimal amount) {
        logger.info("Request received: GET /api/employees/salary-threshold?amount={}", amount);
        List<EmployeeResponseDto> employees = EmployeeMapper.toDtoList(employeeService.getEmployeesWithSalaryAbove(amount));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * POST - Apply custom processor using Functional Interface
     * HTTP Status: 200 OK
     */
    @PostMapping("/process")
    public ResponseEntity<List<EmployeeResponseDto>> processEmployees(
            @RequestParam(required = false) String operation) {
        logger.info("Request received: POST /api/employees/process?operation={}", operation);

        EmployeeProcessor processor = employee -> {
            if ("INCREMENT_SALARY".equals(operation)) {
                BigDecimal increment = employee.getSalary()
                        .multiply(new BigDecimal("0.05"));
                employee.setSalary(employee.getSalary().add(increment));
            }
            return employee;
        };

        List<EmployeeResponseDto> processedEmployees = EmployeeMapper.toDtoList(employeeService.processEmployees(processor));
        return new ResponseEntity<>(processedEmployees, HttpStatus.OK);
    }
}

