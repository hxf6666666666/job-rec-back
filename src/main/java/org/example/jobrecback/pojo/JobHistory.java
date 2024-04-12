package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

//浏览历史
@Getter
@Setter
@Entity
@Table(name = "job_history")
public class JobHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "recruitmentId", nullable = false)
    private Long recruitmentId;

    // 发布时间
    @Column(name = "createTime", nullable = false)
    private Instant createTime;

}
