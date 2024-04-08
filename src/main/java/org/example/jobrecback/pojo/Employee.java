package org.example.jobrecback.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/*
 * 简历内容（求职者）表
 * userId -> employeeId -> resumeId
 */

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {
    // 求职者主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 用户id
    @Column(name = "userId", nullable = false)
    private Long userId;

    // 真实姓名
    @Column(name = "realName", nullable = false, length = 50)
    private String realName;

    // 性别
    @Column(name = "gender", nullable = false)
    private Byte gender;

    // 年龄
    @Column(name = "age", nullable = false)
    private Integer age;

    // 生日
    @Column(name = "dateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    // 城市id
    @Column(name = "city", nullable = false)
    private String city;

    //详细地址
    @Column(name = "address", nullable = false, length = 256)
    private String address;

    // 电话
    @Column(name = "userPhone", length = 512)
    private String userPhone;

    // 邮箱
    @Column(name = "email", length = 512)
    private String email;

    // QQ号码
    @Column(name = "qqNumber", length = 20)
    private String qqNumber;

    // 微信号码
    @Column(name = "wechat", length = 50)
    private String wechat;

    // 技能标签，数量不定
    @Column(name = "skillTag", nullable = false, length = 512)
    private String skillTag;

    /// 奖项标签，数量不定
    @Column(name = "awardTag", length = 512)
    private String awardTag;

    // 素养标签，数量不定
    @Column(name = "personalityTag", length = 512)
    private String personalityTag;

    // 补充信息
    @Lob
    @Column(name = "advantage")
    private String advantage;

    // 工作年限
    @Column(name = "workExperienceYear", nullable = false)
    private Integer workExperienceYear;

    // 发布时间
    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    // 更新时间
    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    // 教育经历
    @JsonIgnoreProperties(value = {"educationExperiences"})
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EducationExperience> educationExperiences;

    public List<EducationExperience> getEducationExperiences() {
        return educationExperiences;
    }

    public void setEducationExperiences(List<EducationExperience> educationExperiences) {
        this.educationExperiences = educationExperiences;
    }

//    @Column(name = "education", nullable = false)
//    private Integer education;
//
//    @Column(name = "graduateYear", nullable = false)
//    private Integer graduateYear;
//
//    @Column(name = "schoolName", nullable = false, length = 256)
//    private String schoolName;
//
//    @Column(name = "majorName", nullable = false, length = 256)
//    private String majorName;
//
//    @Column(name = "GPA", precision = 3, scale = 2)
//    private BigDecimal gpa;
//
//    @Column(name = "ranking")
//    private Integer ranking;
//
//    @Column(name = "updateTime", nullable = false)
//    private Instant updateTime;
}