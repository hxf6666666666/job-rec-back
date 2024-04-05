package org.example.jobrecback.dao;

import org.example.jobrecback.pojo.EducationExperience;
import org.example.jobrecback.pojo.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationExperienceRepository extends JpaRepository<EducationExperience, Long>, JpaSpecificationExecutor<EducationExperience> {
    List<EducationExperience> findByEmployeeId(Long employeeId);
}
