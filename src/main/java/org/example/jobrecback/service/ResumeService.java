package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    Resume findByEmployeeId(Long employeeId);

    String uploadResume(MultipartFile file, Long employeeId);

    void delete(Long id);

    List<Resume> findAll();

    List<Resume> searchResume(String fileName,String uploaderName);
}
