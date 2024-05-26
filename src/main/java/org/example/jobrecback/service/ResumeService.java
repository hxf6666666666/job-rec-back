package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ResumeService {

    Resume findByEmployeeId(Long employeeId);

    String uploadResume(MultipartFile file, Long employeeId);

    void delete(Long id);

    List<Resume> findAll();

    List<Resume> searchResume(String fileName,String uploaderName);
    Page<Resume> findAllByPage(Pageable pageable);
    Page<Resume> searchResumeByPage(Pageable pageable,String fileName,String uploaderName);
}
