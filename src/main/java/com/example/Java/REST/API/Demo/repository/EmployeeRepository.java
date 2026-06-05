package com.example.Java.REST.API.Demo.repository;

import com.example.Java.REST.API.Demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

//why this is empty? because it extends JpaRepository which provides basic CRUD operations for the Employee entity, so we don't need to define any methods here unless we want to add custom queries.