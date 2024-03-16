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
@Table(name = "recruitment_favour")
public class RecruitmentFavour {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "employeeId", nullable = false)
    private Long employeeId;

    @Column(name = "jobId", nullable = false)
    private Long jobId;

    @Column(name = "createTime")
    private Instant createTime;

    @Column(name = "updateTime")
    private Instant updateTime;

}