package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
/*
 * 简历表
 *
 */

@Getter
@Setter
@Entity
@Table(name = "resume")
public class Resume {
    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 用户id（上传者）
    @Column(name = "employee_id")
    private Long employeeId;

    // 简历文件名称
    @Column(name = "file_name")
    private String fileName;

    // 简历文件大小
    @Column(name = "file_size")
    private Long fileSize;

    // 简历保存位置（在application.yml中配置）
    @Column(name = "file_path")
    private String filePath;

    // 简历上传（更新）时间
    @Column(name = "upload_time")
    private Instant uploadTime;
}

