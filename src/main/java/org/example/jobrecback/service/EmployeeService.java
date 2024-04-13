package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Long findIdByUserId(Long userId);

    List<Employee> findAll();

    Employee findById(Long id);

    Employee findByUserId(Long userId);

    String uploadEmployee(Employee employee, Long userId);

    void delete(Long id);
    Employee findByIdDecrypt(Long id);

    Employee findByUserIdDecrypt(Long userId);

    List<Employee> findAllDecrypt();
    Page<Employee> findAllDecryptByPage(Pageable pageable);
}
