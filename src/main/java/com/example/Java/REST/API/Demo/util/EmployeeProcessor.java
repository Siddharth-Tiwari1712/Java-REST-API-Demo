package com.example.Java.REST.API.Demo.util;

import com.example.Java.REST.API.Demo.entity.Employee;

/**
 * FUNCTIONAL INTERFACE - Custom Functional Interface
 * 
 * A functional interface is an interface with exactly ONE abstract method.
 * It can have default methods and static methods, but only ONE abstract method.
 * 
 * This allows us to use Lambda expressions and method references when implementing it.
 */
@FunctionalInterface
public interface EmployeeProcessor {
    
    /**
     * Single abstract method - this makes it a functional interface
     * Processes an employee and returns the result
     */
    Employee process(Employee employee);
}
