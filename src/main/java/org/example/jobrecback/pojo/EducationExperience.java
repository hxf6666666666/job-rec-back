package org.example.jobrecback.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

//    @Column(name = "employeeId", nullable = false)
//    private Long employeeId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

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

    @Column(name = "isDelete")
    private Byte isDelete;

}