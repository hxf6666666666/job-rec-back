package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {
    String uploadResume(MultipartFile file, Long userId);

    List<Resume> findResumeAll(String fileName, Integer resumeStatus, Long userId);

    void  deleteResume(Long resumeId);
}
