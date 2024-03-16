package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "education_experience")
public class EducationExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "employeeId", nullable = false)
    private Long employeeId;

    @Column(name = "schoolName", nullable = false, length = 256)
    private String schoolName;

    @Column(name = "educationType", nullable = false)
    private Byte educationType;

    @Column(name = "beginYear", nullable = false)
    private Integer beginYear;

    @Column(name = "endYear", nullable = false)
    private Integer endYear;

    @Column(name = "majorName", nullable = false, length = 256)
    private String majorName;

    @Lob
    @Column(name = "activity")
    private String activity;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDelete", nullable = false)
    private Byte isDelete;

}