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
    @Column(name = "id", nullable = false)
    private Long id;

    //职位名称
    @Column(name = "jobName", nullable = false, length = 512)
    private String jobName;

    //发布者id
    @Column(name = "userId", nullable = false)
    private Long userId;

    //公司名称
    @Column(name = "companyName", nullable = false, length = 512)
    private String companyName;

    //职位大类id
    @Column(name = "industryId", nullable = false)
    private Long industryId;

    //职位描述
    @Lob
    @Column(name = "jobDescription", nullable = false)
    private String jobDescription;

    //最低学历要求，编号（0:不限，1：大专，2：本科，3：硕士，4：博士）
    @Column(name = "educationType")
    private Byte educationType;

    //最低工作年限要求，编号（7种，编号0-6）
    @Column(name = "workTimeType")
    private Byte workTimeType;

    // 职位所需素养：前端用标签显示，数量不定
    @Column(name = "jobPersonality", nullable = false, length = 1024)
    private String jobPersonality;

    // 职位所需技能：前端用标签显示，数量不定
    @Column(name = "jobSkills", length = 1024)
    private String jobSkills;

    //0：全职，1:实习
    @Column(name = "jobType", nullable = false)
    private Integer jobType;

    //职位详细地址
    @Column(name = "jobAddress", nullable = false, length = 256)
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

    // 城市id
    @Column(name = "cityId", nullable = false)
    private Long cityId;

    // 发布时间
    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    // 更新时间
    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;
}