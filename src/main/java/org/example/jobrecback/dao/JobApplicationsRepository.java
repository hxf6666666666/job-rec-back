package org.example.jobrecback.dao;

import org.example.jobrecback.pojo.JobApplications;
import org.example.jobrecback.pojo.JobFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobApplicationsRepository extends JpaRepository<JobApplications, Long>, JpaSpecificationExecutor<JobApplications> {

    JobApplications findByUserIdAndRecruitmentId(Long userId, Long recruitmentId);

    @Query("SELECT j.recruitmentId FROM JobApplications j WHERE j.userId = :userId ORDER BY j.createTime DESC")
    List<Long> findRecruitmentIdByUserId(Long userId);
    @Query("SELECT j.userId FROM JobApplications j WHERE j.recruitmentId = :recruitmentId ORDER BY j.createTime DESC")
    List<Long> findUserIdByRecruitmentId(Long recruitmentId);

    @Query("SELECT j.recruitmentId FROM JobApplications j WHERE j.userId = :userId AND j.offerStatus = :offerStatus ORDER BY j.createTime DESC")
    List<Long> findRecruitmentIdByUserIdAndOfferStatus(Long userId,String offerStatus);
    @Query("SELECT j.userId FROM JobApplications j WHERE j.recruitmentId = :recruitmentId AND j.offerStatus = :offerStatus ORDER BY j.createTime DESC")
    List<Long> findUserIdByRecruitmentIdAndOfferStatus(Long recruitmentId, String offerStatus);
}
