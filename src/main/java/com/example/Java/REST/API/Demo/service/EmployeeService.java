package com.example.Java.REST.API.Demo.service;

import com.example.Java.REST.API.Demo.entity.Employee;
import com.example.Java.REST.API.Demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }//find all is a method provided by JpaRepository that retrieves all records from the employee table and returns them as a list of Employee objects.

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existing = getEmployeeById(id);
        existing.setFirstName(updatedEmployee.getFirstName());
        existing.setLastName(updatedEmployee.getLastName());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setJobTitle(updatedEmployee.getJobTitle());
        existing.setSalary(updatedEmployee.getSalary());
        return employeeRepository.save(existing);
    }

    public void deleteEmployee(Long id) {
        Employee existing = getEmployeeById(id);
        employeeRepository.delete(existing);
    }

    //create every employee salary increment by 10% every year
    public void incrementSalaries() {        
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
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
