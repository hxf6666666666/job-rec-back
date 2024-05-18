package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface RecruitmentService {
    Recruitment save(Recruitment recruitment);

    List<Recruitment> findByUserId(Long userId);

    List<Recruitment> findAll();
    List<Recruitment> findAllByJobNameContainingAndIndustryId(String jobName, Long industryId);

    Recruitment findById(Long id);

    List<Recruitment> findAllByIndustryId(Long industryId);

    List<Recruitment> findAllByJobNameContaining(String name);

    List<Recruitment> getMyPosts(Long userId, String name, Long industryId);

    List<Recruitment> search(String name, Integer jobType, String city, Long industryId, Byte workTimeType, Byte salary, Byte educationType);

    void delete(Long id);

    void update(Recruitment recruitment);

    // 提取关键词
    String extractEntitiesFromDescription(String description, String dictPath, int flag) throws IOException;
    Page<Recruitment> findAllByPage(Pageable pageable);
    Page<Recruitment> searchByPage(Pageable pageable,String name, Integer jobType, String city, Long industryId, Byte workTimeType, Byte salary, Byte educationType);

    List<Recruitment> recommend(Employee employee, String name, Integer jobType, String city, Long industryId, Byte workTimeType, Byte salary, Byte educationType);
    Page<Recruitment> search2(String name, Integer jobType, String city, Long industryId,
                              Byte workTimeType, Byte salary, Byte educationType,
                              int page, int pageSize);
    String addFavorites(Long userId,Long recruitmentId);
    String deleteFavorites(Long userId,Long recruitmentId);
    List<Employee> getFavorites(Long recruitmentId);
    String isFavorites(Long userId,Long recruitmentId);
    Long countAll();
}