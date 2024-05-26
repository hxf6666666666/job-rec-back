package org.example.jobrecback.dao;

import org.example.jobrecback.pojo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @Query("SELECT e.id FROM Employee e WHERE e.userId = :userId")
    Long findIdByUserId(Long userId);

    @Query("SELECT e.id FROM Employee e WHERE e.realName = :uploaderName")
    Long findIdByRealName(String uploaderName);

    @Query("SELECT e.id FROM Employee e WHERE e.realName LIKE %:uploaderName%")
    List<Long> findIdsByRealNameContaining(String uploaderName);

    Employee findByUserId(Long userId);
    @Query("SELECT e FROM Employee e ORDER BY e.createTime DESC")
    Page<Employee> findAllOrderByCreateTimeDesc(Pageable pageable);
}
