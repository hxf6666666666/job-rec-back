package org.example.jobrecback.dao;

import org.example.jobrecback.pojo.JobFavorites;
import org.example.jobrecback.pojo.JobHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobFavoritesRepository extends JpaRepository<JobFavorites, Long>, JpaSpecificationExecutor<JobFavorites> {

    JobFavorites findByUserIdAndRecruitmentId(Long userId, Long recruitmentId);

    @Query("select j.recruitmentId FROM JobFavorites j WHERE j.userId = :userId")
    List<Long> findRecruitmentIdByUserId(Long userId);
}
