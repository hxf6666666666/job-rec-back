package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "talent_favour")
public class TalentFavour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "employeeId", nullable = false)
    private Long employeeId;

    @Column(name = "createTime")
    private Instant createTime;

    @Column(name = "updateTime")
    private Instant updateTime;

}