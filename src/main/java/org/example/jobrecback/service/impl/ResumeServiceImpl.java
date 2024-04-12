package org.example.jobrecback.service.impl;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.example.jobrecback.dao.EmployeeRepository;
import org.example.jobrecback.dao.ResumeRepository;
import org.example.jobrecback.pojo.Resume;
import org.example.jobrecback.service.ResumeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class ResumeServiceImpl implements ResumeService {
    @Resource
    private ResumeRepository resumeRepository;
    @Resource
    private EmployeeRepository employeeRepository;
    @Value("${resume.upload.directory}")
    private String uploadDirectory;
    @Override
    public Resume findByEmployeeId(Long employeeId) {
        return resumeRepository.findByEmployeeId(employeeId);
    }
    @Transactional
    @Override
    public String uploadResume(MultipartFile file, Long employeeId) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return "上传的简历为空";
        }
        // 检查文件大小
        long maxFileSize = 15 * 1024 * 1024; // 15MB
        if (file.getSize() > maxFileSize) {
            return "上传的简历文件过大，请上传小于15MB的文件";
        }

        // 检查文件类型
        String[] allowedFileTypes = {"pdf", "doc", "docx"};
        String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        if (!isValidFileType(fileExtension, allowedFileTypes)) {
            return "不支持上传的简历文件类型";
        }
        String fileExtensionLowerCase = fileExtension.toLowerCase();
        String filePath = employeeId + "." + fileExtensionLowerCase;

        // 检查数据库中是否存在该用户的简历
        Optional<Resume> existingResumeOptional = Optional.ofNullable(resumeRepository.findByEmployeeId(employeeId));
        if (existingResumeOptional.isPresent()) {
            // 如果存在，先删除旧简历文件
            Resume existingResume = existingResumeOptional.get();
            String existingFilePath = existingResume.getFilePath();
            File existingFile = new File(existingFilePath);
            if (existingFile.exists()) {
                boolean deleteResult = existingFile.delete();
                if (!deleteResult) {
                    log.error("删除旧简历文件失败: " + existingFilePath);
                }
            }
            // 更新数据库中的简历信息
            existingResume.setFileName(file.getOriginalFilename());
            existingResume.setFileSize(file.getSize());
            existingResume.setUploadTime(Instant.now());
            existingResume.setFilePath(uploadDirectory + filePath);
            resumeRepository.save(existingResume);
        } else {
            // 如果不存在，保存新的简历信息到数据库
            Resume resume = new Resume();
            resume.setEmployeeId(employeeId);
            resume.setFileName(file.getOriginalFilename());
            resume.setFileSize(file.getSize());
            resume.setUploadTime(Instant.now());
            resume.setFilePath(uploadDirectory + filePath);
            resumeRepository.save(resume);
        }

        // 保存简历文件到存储文件夹
        File dest = new File(uploadDirectory + filePath);
        if (!dest.getParentFile().exists()) {
            boolean mkdirsResult = dest.getParentFile().mkdirs();
            if (!mkdirsResult) {
                log.error("创建目录失败: " + dest.getParentFile().getAbsolutePath());
            }
        }
        try {
            file.transferTo(dest);
            return "简历上传成功";
        } catch (IOException e) {
            return "简历上传失败";
        }
    }
    @Transactional
    @Override    //删除简历（物理删除+数据库删除）
    public void delete(Long id) {
        Optional<Resume> resumeOptional = resumeRepository.findById(id);
        if (resumeOptional.isPresent()) {
            Resume resume = resumeOptional.get();
            File file = new File(resume.getFilePath());
            if (file.exists()) {
                boolean deleteResult = file.delete();
                if (deleteResult) {
                    resumeRepository.deleteById(id);
                } else {
                    log.error("文件删除失败: " + resume.getFileName());
                }
            } else {
                // 文件不存在，可能已经被手动删除，直接删除数据库中的记录
                resumeRepository.deleteById(id);
            }
        }
    }

    @Override
    public List<Resume> findAll() {
        return resumeRepository.findAll();
    }

    @Transactional
    @Override
    public List<Resume> searchResume(String fileName, String uploaderName) {
        // 根据uploaderName从employee表中根据realName找到所有匹配的employeeId
        List<Long> employeeIds = Collections.emptyList();
        if (StringUtils.hasText(uploaderName)) {
            employeeIds = employeeRepository.findIdsByRealNameContaining(uploaderName);
        }

        // 根据找到的employeeId和fileName找到对应的resume
        List<Long> finalEmployeeIds = employeeIds;
        Specification<Resume> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!finalEmployeeIds.isEmpty()) {
                predicates.add(root.get("employeeId").in(finalEmployeeIds));
            }
            if (StringUtils.hasText(fileName)) {
                predicates.add(cb.like(root.get("fileName"), "%" + fileName + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return resumeRepository.findAll(spec);
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex + 1).toLowerCase();
    }

    private boolean isValidFileType(String fileExtension, String[] allowedFileTypes) {
        for (String type : allowedFileTypes) {
            if (type.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}
