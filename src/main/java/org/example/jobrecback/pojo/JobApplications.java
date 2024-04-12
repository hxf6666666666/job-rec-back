package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "job_applications")
public class JobApplications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "recruitmentId", nullable = false)
    private Long recruitmentId;

    //offer状态
    @Column(name = "offerStatus", nullable = false)
    private String offerStatus;

    // 发布时间
    @Column(name = "createTime", nullable = false)
    private Instant createTime;

}
