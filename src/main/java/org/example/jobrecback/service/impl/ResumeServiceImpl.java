package org.example.jobrecback.service.impl;

import jakarta.annotation.Resource;
import org.example.jobrecback.dao.ResumeRepository;
import org.example.jobrecback.pojo.Resume;
import org.example.jobrecback.service.ResumeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.Objects;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Resource
    private ResumeRepository resumeRepository;

    @Value("${resume.upload.directory}")
    private String uploadDirectory;

    public String uploadResume(MultipartFile file, Long userId) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return "上传的简历为空";
        }

        // 获取文件名
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // 保存文件到数据库
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setFileName(fileName);
        resume.setFileSize(file.getSize());
        resume.setUploadTime(new Date(System.currentTimeMillis()));
        resume.setFilePath(uploadDirectory + fileName);
        resume.setResumeStatus(0);
        resume.setIsDeleted(0);
        resumeRepository.save(resume);

        return "简历上传成功";
    }
}
