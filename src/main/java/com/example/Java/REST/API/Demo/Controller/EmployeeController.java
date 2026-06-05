package com.example.Java.REST.API.Demo.controller;

import com.example.Java.REST.API.Demo.entity.Employee;
import com.example.Java.REST.API.Demo.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//http request
@RequestMapping("/api/employees")//path
public class EmployeeController {

    //final for immutability
    private final EmployeeService employeeService;

    //constructor injection for better testability and immutability
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // not efficient approach
    // @Autowired
    // private EmployeeService employeeService;

    //below method act as http request handler for get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee created = employeeService.createEmployee(employee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
