package com.example.Java.REST.API.Demo.dto;

import com.example.Java.REST.API.Demo.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new Employee(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getJobTitle(),
                dto.getSalary());
    }

    public static EmployeeResponseDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getJobTitle(),
                employee.getSalary());
    }

    public static List<EmployeeResponseDto> toDtoList(List<Employee> employees) {
        return employees.stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }
}
