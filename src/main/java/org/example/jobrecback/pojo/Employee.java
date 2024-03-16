package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "resumePhoto", length = 1024)
    private String resumePhoto;

    @Column(name = "biography", length = 1024)
    private String biography;

    @Column(name = "realName", nullable = false, length = 50)
    private String realName;

    @Column(name = "gender", nullable = false)
    private Byte gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "dateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "cityId", nullable = false)
    private Long cityId;

    @Column(name = "userPhone", length = 512)
    private String userPhone;

    @Column(name = "email", length = 512)
    private String email;

    @Column(name = "qqNumber", length = 20)
    private String qqNumber;

    @Column(name = "wechat", length = 50)
    private String wechat;

    @Column(name = "skillTag", nullable = false, length = 512)
    private String skillTag;

    @Column(name = "languageTag", length = 512)
    private String languageTag;

    @Column(name = "competencyTag", length = 512)
    private String competencyTag;

    @Lob
    @Column(name = "advantage")
    private String advantage;

    @Column(name = "qualificationIds", length = 1024)
    private String qualificationIds;

    @Column(name = "education", nullable = false)
    private Integer education;

    @Column(name = "graduateYear", nullable = false)
    private Integer graduateYear;

    @Column(name = "schoolName", nullable = false, length = 256)
    private String schoolName;

    @Column(name = "majorName", nullable = false, length = 256)
    private String majorName;

    @Column(name = "GPA", precision = 3, scale = 2)
    private BigDecimal gpa;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "workExperienceYear", nullable = false)
    private Integer workExperienceYear;

    @Column(name = "jobStatus", nullable = false)
    private Byte jobStatus;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDelete", nullable = false)
    private Byte isDelete;

}