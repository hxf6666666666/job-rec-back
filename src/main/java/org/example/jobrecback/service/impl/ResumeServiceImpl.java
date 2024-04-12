package org.example.jobrecback.service.impl;

import com.qingstor.sdk.config.EnvContext;
import com.qingstor.sdk.exception.QSException;
import com.qingstor.sdk.service.Bucket;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.example.jobrecback.config.QingStorConfig;
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
import java.io.FileNotFoundException;
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
    @Resource
    private QingStorConfig qingStorConfig;

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
        String[] allowedFileTypes = {"pdf", "doc", "docx", "png", "jpg", "txt"};
        String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        if (!isValidFileType(fileExtension, allowedFileTypes)) {
            return "不支持上传的简历文件类型";
        }

        String filePath = file.getOriginalFilename();


        // 检查数据库中是否存在该用户的简历
        Optional<Resume> existingResumeOptional = Optional.ofNullable(resumeRepository.findByEmployeeId(employeeId));
        if (existingResumeOptional.isPresent()) {
            // 如果存在，先删除旧简历文件
            Resume existingResume = existingResumeOptional.get();
            EnvContext env = new EnvContext(qingStorConfig.getAccessKeyId(),qingStorConfig.getSecretAccessKey());
            String zoneKey = "pek3b";
            String bucketName = "hexinfeng";
            Bucket bucket = new Bucket(env, zoneKey, bucketName);
            try {
                Bucket.DeleteObjectOutput output = bucket.deleteObject(existingResume.getFilePath(), null);
            } catch (QSException e) {
                throw new RuntimeException(e);
            }
            // 更新数据库中的简历信息
            existingResume.setFileName(file.getOriginalFilename());
            existingResume.setFileSize(file.getSize());
            existingResume.setUploadTime(Instant.now());
            existingResume.setFilePath("resume/" + file.getOriginalFilename());
            resumeRepository.save(existingResume);
        } else {
            // 如果不存在，保存新的简历信息到数据库
            Resume resume = new Resume();
            resume.setEmployeeId(employeeId);
            resume.setFileName(file.getOriginalFilename());
            resume.setFileSize(file.getSize());
            resume.setUploadTime(Instant.now());
            resume.setFilePath("resume/" + filePath);
            resumeRepository.save(resume);
        }
        EnvContext env = new EnvContext(qingStorConfig.getAccessKeyId(),qingStorConfig.getSecretAccessKey());
        String zoneKey = "pek3b";
        String bucketName = "hexinfeng";
        Bucket bucket = new Bucket(env, zoneKey, bucketName);
        // 获取文件名
        String fileName = "resume/" + file.getOriginalFilename();
        // 获取文件后缀(.xml)
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 若要防止生成的临时文件重复,需要在文件名后添加随机码
        File tempFile = null;
        try {
            tempFile = File.createTempFile(fileName, suffix);
            file.transferTo(tempFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Bucket.PutObjectInput input = new Bucket.PutObjectInput();
            input.setBodyInputFile(tempFile);
            bucket.putObject(fileName, input);
            tempFile.deleteOnExit();
            return "简历上传成功";
        } catch (QSException e) {
            return "简历上传失败";
        }
    }

    @Transactional
    @Override    //删除简历（物理删除+数据库删除）
    public void delete(Long id) {
        Optional<Resume> resumeOptional = resumeRepository.findById(id);
        if (resumeOptional.isPresent()) {
            Resume resume = resumeOptional.get();
            EnvContext env = new EnvContext(qingStorConfig.getAccessKeyId(),qingStorConfig.getSecretAccessKey());
            String zoneKey = "pek3b";
            String bucketName = "hexinfeng";
            Bucket bucket = new Bucket(env, zoneKey, bucketName);
            try {
                Bucket.DeleteObjectOutput output = bucket.deleteObject(resume.getFilePath(), null);
            } catch (QSException e) {
                throw new RuntimeException(e);
            }
            resumeRepository.deleteById(id);
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
