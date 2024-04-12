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
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "schoolName")
    private String schoolName;

    @Column(name = "schoolType")
    private String schoolType;

    @Column(name = "educationType")
    private Byte educationType;

    @Column(name = "beginYear")
    private Integer beginYear;

    @Column(name = "endYear")
    private Integer endYear;

    @Column(name = "majorName")
    private String majorName;

    @Column(name = "activity")
    private String activity;

    @Column(name = "gpa")
    private String gpa;

    @Column(name = "ranking")
    private String ranking;
}