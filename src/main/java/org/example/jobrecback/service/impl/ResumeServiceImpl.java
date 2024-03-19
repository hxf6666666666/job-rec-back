package org.example.jobrecback.service.impl;

import jakarta.annotation.Resource;
import org.example.jobrecback.dao.ResumeRepository;
import org.example.jobrecback.pojo.Resume;
import org.example.jobrecback.service.ResumeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
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
        resume.setUploadTime(Instant.now());
        resume.setFilePath(uploadDirectory + fileName);
        resume.setResumeStatus(0);
        resume.setIsDeleted(0);
        resumeRepository.save(resume);
        File dest = new File(uploadDirectory + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return "简历上传成功";
        } catch (IOException e) {
            return "简历上传失败";
        }
    }

    @Override
    public List<Resume> findResumeAll(String fileName, Integer resumeStatus, Long userId) {
        return resumeRepository.findByFileNameContainingAndResumeStatus(fileName, resumeStatus, userId);
    }

    @Override
    public void deleteResume(Long resumeId) {
        Resume resume = resumeRepository.findResumeById(resumeId);
        String filePath = resume.getFilePath();
        // 删除文件
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
        resumeRepository.deleteResumeById(resumeId);
    }

}
