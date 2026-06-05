package com.example.Java.REST.API.Demo.entity;//package declaration, indicates that this class is part of the com.example.Java.REST.API.Demo.entity package

import jakarta.persistence.*;//JPA annotations for entity mapping
import java.math.BigDecimal;

//package
//library

//lombok annotations for boilerplate code reduction (optional)
//getters setters

@Entity//specifies that the class is an entity and is mapped to a database table
@Table(name = "employee")//specifies the name of the database table to be used for mapping
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)//nullable = false means this column cannot be null in the database
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(nullable = false)
    private BigDecimal salary;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, String jobTitle, BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.jobTitle = jobTitle;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
