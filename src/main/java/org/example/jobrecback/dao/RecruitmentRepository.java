package org.example.jobrecback.dao;

import org.example.jobrecback.pojo.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, JpaSpecificationExecutor<Recruitment> {

    List<Recruitment> findByUserId(Long userId);

    List<Recruitment> findAllByJobNameContainingAndIndustryIdOrderByCreateTimeDesc(@Param("jobName")String jobName, @Param("industryId")Long industryId);

    List<Recruitment> findAllByIndustryId(Long industryId);

    List<Recruitment> findAllByJobNameContaining(String name);

    List<Recruitment> findByIdIn(List<Long> recruitmentIds);
}