package org.example.jobrecback.pojo;

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

    // 简历完整度评分
    @Column(name = "resumeIntegrity")
    private Double resumeIntegrity;

    // 简历头像
    @Column(name = "avatar",columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String avatar;

    // 用户id
    @Column(name = "userId", nullable = false)
    private Long userId;

    // 真实姓名
    @Column(name = "realName", length = 50)
    private String realName;

    // 性别
    @Column(name = "gender")
    private Byte gender;

    // 年龄
    @Column(name = "age")
    private Integer age;

    // 生日
    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;

    // 城市
    @Column(name = "city")
    private String city;

    //详细地址
    @Column(name = "address", length = 256)
    private String address;

    // 电话
    @Column(name = "userPhone", length = 512)
    private String userPhone;

    // 邮箱
    @Column(name = "email", length = 512)
    private String email;

    // QQ号码
    @Column(name = "qqNumber", length = 128)
    private String qqNumber;

    // 微信号码
    @Column(name = "wechat", length = 128)
    private String wechat;

    // 技能标签，数量不定
    @Column(name = "skillTag", length = 512)
    private String skillTag;

    /// 奖项标签，数量不定
    @Column(name = "awardTag", length = 512)
    private String awardTag;

    // 素养标签，数量不定
    @Column(name = "personalityTag", length = 512)
    private String personalityTag;

    // 原始信息，rawText
    @Column(name = "advantage")
    private String advantage;

    // 工作年限
    @Column(name = "workExperienceYear")
    private Integer workExperienceYear;

    @Column(name = "englishTag")
    private String englishTag;

    // 发布时间
    @Column(name = "createTime")
    private Instant createTime;

    // 更新时间
    @Column(name = "updateTime")
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
//
//    // 最高教育经历
//    @Column(name = "schoolName")
//    private String schoolName;
//
//    @Column(name = "majorName")
//    private String majorName;
//
//    @Column(name = "degree")
//    private Integer degree;
//
//    @Column(name = "schoolType")
//    private String schoolType;
}