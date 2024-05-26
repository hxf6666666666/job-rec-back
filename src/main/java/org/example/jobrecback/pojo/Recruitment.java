package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/*
 * 职位表
 *
 */

@Builder
@Getter
@Setter
@Entity
@Table(name = "recruitment")
public class Recruitment {
    // 职位主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    //职位名称
    @Column(name = "jobName", nullable = false, length = 512)
    private String jobName;

    //发布者id
    @Column(name = "userId", nullable = false)
    private Long userId;

    //公司名称
    @Column(name = "companyName", length = 512)
    private String companyName;

    //职位大类id
    @Column(name = "industryId")
    private Long industryId;

    //职位描述
    @Column(name = "jobDescription", length = 1024, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String jobDescription;

    //最低学历要求，编号（0:不限，1：大专，2：本科，3：硕士，4：博士）
    @Column(name = "educationType")
    private Byte educationType;

    //最低工作年限要求，编号（7种，编号0-6）
    @Column(name = "workTimeType")
    private Byte workTimeType;

    // 职位所需素养：前端用标签显示，数量不定
    @Column(name = "jobPersonality", length = 1024)
    private String jobPersonality;

    // 职位所需技能：前端用标签显示，数量不定
    @Column(name = "jobSkills", length = 1024)
    private String jobSkills;

    //0：全职，1:实习
    @Column(name = "jobType")
    private Integer jobType;

    //职位详细地址
    @Column(name = "jobAddress",length = 256)
    private String jobAddress;

    //薪水上限
    @Column(name = "salaryUpper")
    private Integer salaryUpper;

    //薪水下限
    @Column(name = "salaryLower")
    private Integer salaryLower;

    //薪水类型：12、13、14、15、16、17薪...，默认12
    @Builder.Default
    @Column(name = "salaryUnit")
    private Byte salaryUnit = 12;

    // 职位原始链接
    @Column(name = "link")
    private String link;

    // 城市
    @Column(name = "city")
    private String city;

    // 发布时间
    @Column(name = "createTime")
    private Instant createTime;

    // 更新时间
    @Column(name = "updateTime")
    private Instant updateTime;

    public Recruitment() {
        this.salaryUnit = 12;
    }

    public Recruitment(Long id, String jobName, Long userId, String companyName, Long industryId, String jobDescription,
                       Byte educationType, Byte workTimeType, String jobPersonality, String jobSkills, Integer jobType,
                       String jobAddress, Integer salaryUpper, Integer salaryLower, Byte salaryUnit, String link,
                       String city, Instant createTime, Instant updateTime) {
        this.id = id;
        this.jobName = jobName;
        this.userId = userId;
        this.companyName = companyName;
        this.industryId = industryId;
        this.jobDescription = jobDescription;
        this.educationType = educationType;
        this.workTimeType = workTimeType;
        this.jobPersonality = jobPersonality;
        this.jobSkills = jobSkills;
        this.jobType = jobType;
        this.jobAddress = jobAddress;
        this.salaryUpper = salaryUpper;
        this.salaryLower = salaryLower;
        this.salaryUnit = salaryUnit;
        this.link = link;
        this.city = city;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}