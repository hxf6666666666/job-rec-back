package org.example.jobrecback.service;

import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {
    String uploadResume(MultipartFile file, Long userId);
}
