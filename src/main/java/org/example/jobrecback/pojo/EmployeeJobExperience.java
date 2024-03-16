package org.example.jobrecback.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "employee_job_experience")
public class EmployeeJobExperience {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "employeeId", nullable = false)
    private Long employeeId;

    @Column(name = "companyName", nullable = false, length = 512)
    private String companyName;

    @Column(name = "beginTime", nullable = false, length = 256)
    private String beginTime;

    @Column(name = "endTime", nullable = false, length = 256)
    private String endTime;

    @Column(name = "jobRole", nullable = false, length = 256)
    private String jobRole;

    @Column(name = "experienceDescript", nullable = false, length = 512)
    private String experienceDescript;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDelete", nullable = false)
    private Byte isDelete;

}