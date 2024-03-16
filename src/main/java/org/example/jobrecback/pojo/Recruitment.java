package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "recruitment")
public class Recruitment {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "jobName", nullable = false, length = 512)
    private String jobName;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "companyName", nullable = false, length = 512)
    private String companyName;

    @Column(name = "industryId", nullable = false)
    private Long industryId;

    @Lob
    @Column(name = "jobDescription", nullable = false)
    private String jobDescription;

    @Column(name = "educationType")
    private Byte educationType;

    @Column(name = "jobPersonality", nullable = false, length = 1024)
    private String jobPersonality;

    @Column(name = "jobSkills", length = 1024)
    private String jobSkills;

    @Column(name = "jobType", nullable = false)
    private Integer jobType;

    @Column(name = "jobAddress", nullable = false, length = 256)
    private String jobAddress;

    @Column(name = "salaryUpper")
    private Integer salaryUpper;

    @Column(name = "salaryLower")
    private Integer salaryLower;

    @Column(name = "salaryUnit")
    private Byte salaryUnit;

    @Column(name = "cityId", nullable = false)
    private Long cityId;

    @Column(name = "jobActive", nullable = false)
    private Byte jobActive;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDelete", nullable = false)
    private Byte isDelete;

}