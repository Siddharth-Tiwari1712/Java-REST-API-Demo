// The package declaration:
// tells Java:

// "This file lives in this folder/namespace."
// Java uses that information to organize code, resolve imports, and help frameworks like Spring locate your classes.

package com.example.Java.REST.API.Demo;

// SpringApplication is used to launch the Spring Boot application.
// @SpringBootApplication is a convenience annotation that combines several important Spring annotations.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This annotation is equivalent to:

// @Configuration - Tells Spring that this class can contain bean definitions.
// @EnableAutoConfiguration - Look at the dependencies in my project and automatically configure what I need.
// @ComponentScan - Search for Spring components in this package and its sub-packages.
@SpringBootApplication
public class JavaRestApiDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaRestApiDemoApplication.class, args);
	}

}

// This is the standard Java entry point.

// When executed:

// Creates the Spring Application Context.
// Performs component scanning.
// Applies auto-configuration.
// Creates all Spring-managed beans.
// Starts the embedded web server (Tomcat by default).
// Makes your REST endpoints available.
