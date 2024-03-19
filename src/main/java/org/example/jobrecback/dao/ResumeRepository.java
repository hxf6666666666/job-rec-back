package org.example.jobrecback.dao;

import jakarta.transaction.Transactional;
import org.example.jobrecback.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    @Query("SELECT r FROM Resume r WHERE (:fileName IS NULL OR r.fileName LIKE %:fileName%) AND (:resumeStatus IS NULL OR r.resumeStatus = :resumeStatus) AND (r.userId = :userId) order by r.uploadTime desc")
    List<Resume> findByFileNameContainingAndResumeStatus(
           @Param("fileName") String fileName,
           @Param("resumeStatus") Integer resumeStatus,
           @Param("userId") Long userId);

    Resume findResumeById(Long id);

    @Transactional
    void deleteResumeById(@Param("id") Long id);
}