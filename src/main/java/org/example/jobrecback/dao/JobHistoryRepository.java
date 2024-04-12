package org.example.jobrecback.dao;

import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.JobHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long>, JpaSpecificationExecutor<JobHistory> {
    JobHistory findByUserIdAndRecruitmentId(Long userId, Long recruitmentId);

    @Query("SELECT j.recruitmentId FROM JobHistory j WHERE j.userId = :userId ORDER BY j.createTime DESC")
    List<Long> findRecruitmentIdByUserId(Long userId);
}
