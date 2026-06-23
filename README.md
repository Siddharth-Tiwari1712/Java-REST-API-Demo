# Spring Boot Employee Management REST API

## Overview

A RESTful Employee Management System built using Spring Boot and Java 21.

This project demonstrates:

* Spring Boot REST APIs
* Layered Architecture (Controller, Service, Repository)
* DTO Pattern
* Mapper Pattern
* Exception Handling
* Java Stream API
* Lambda Expressions
* Functional Interfaces
* Spring Security
* JWT Authentication & Authorization
* Validation
* JPA/Hibernate
* MySQL Database

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Security
* JWT
* MySQL
* Maven
* Lombok (Optional)
* Postman

---

## Project Structure

src/main/java

com.example.employee

├── controller

├── service

├── repository

├── entity

├── dto

├── mapper

├── security

├── exception

├── util

└── config

---

## Features

### Authentication

POST /api/auth/register

POST /api/auth/login

Uses JWT token generation and validation.

---

### Employee APIs

GET /api/employees

GET /api/employees/{id}

POST /api/employees

PUT /api/employees/{id}

DELETE /api/employees/{id}

---

## Entity

Employee

* id
* firstName
* lastName
* email
* jobTitle
* salary

---

## DTO Pattern

### EmployeeRequestDto

Used for incoming requests.

### EmployeeResponseDto

Used for outgoing responses.

Benefits:

* Hide internal fields
* Validate input
* Decouple API from database model

---

## Mapper Pattern

EmployeeMapper

Methods:

* toEntity()
* toDto()
* toDtoList()

Example:

Employee employee =
EmployeeMapper.toEntity(requestDto);

EmployeeResponseDto dto =
EmployeeMapper.toDto(employee);

---

## Validation

Example:

@NotBlank

@Email

@Positive

If validation fails:

HTTP 400 Bad Request

---

## Exception Handling

Custom Exceptions:

EmployeeNotFoundException

DuplicateEmailException

Global Handler:

@RestControllerAdvice

@ExceptionHandler

Example Response:

{
"timestamp":"2026-06-23",
"status":404,
"message":"Employee not found"
}

---

## Lambda Expressions

Example:

EmployeeProcessor processor =
employee -> {
employee.setSalary(
employee.getSalary().multiply(
BigDecimal.valueOf(1.10)
));
return employee;
};

---

## Functional Interface

@FunctionalInterface

public interface EmployeeProcessor {

Employee process(Employee employee);

}

Purpose:

Allows business rules to be passed dynamically.

---

## Stream API

Get all employee names:

employees.stream()

.map(Employee::getFirstName)

.toList();

---

Find high-paid employees:

employees.stream()

.filter(emp ->
emp.getSalary()
.compareTo(
BigDecimal.valueOf(100000)
) > 0)

.toList();

---

Sort employees:

employees.stream()

.sorted(
Comparator.comparing(
Employee::getSalary
))

.toList();

---

## JWT Authentication

Login Flow

1. User sends username/password
2. Server validates credentials
3. JWT token generated
4. Token returned
5. Client sends token in Authorization header

Authorization: Bearer <jwt-token>

---

## Security Configuration

Public APIs

/api/auth/**

Protected APIs

/api/employees/**

JWT filter validates every request.

---

## Sample Login Response

{
"token":"eyJhbGciOiJIUzI1NiJ9..."
}

---

## Database

Employee Table

id

first_name

last_name

email

job_title

salary

---

## Learning Outcomes

After completing this project you will understand:

* Spring Boot fundamentals
* REST API development
* DTO and Mapper patterns
* Dependency Injection
* Stream API
* Lambda Expressions
* Functional Interfaces
* Exception Handling
* JWT Authentication
* Spring Security
* JPA/Hibernate
* Clean Architecture





->>>>>>Login Phase


Client → /api/auth/login
→ Sends username & password


AuthController
→ Calls authenticationManager.authenticate()


AuthenticationManager
→ Checks DB via UserDetailsService


JwtTokenProvider
→ Generates JWT token


AuthController
→ Returns token (JwtResponseDto)



->>>>>>>Request Phase (After Login)


Client → API request
→ Sends Authorization: Bearer <JWT>


JwtAuthenticationFilter
→ Intercepts request before controller


JwtTokenProvider
→ Validates token (no DB by default)


SecurityContext
→ Sets authenticated user


Controller (any POST/GET/PUT)
→ Executes only if token is valid ✅


