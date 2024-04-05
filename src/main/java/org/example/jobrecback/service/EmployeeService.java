package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Employee;

import java.util.List;

public interface EmployeeService {
    Long findIdByUserId(Long userId);

    List<Employee> findAll();

    Employee findById(Long id);

    Employee findByUserId(Long userId);

    String uploadEmployee(Employee employee, Long userId);

    void delete(Long id);
}
