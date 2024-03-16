package org.example.jobrecback.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "project_experience")
public class ProjectExperience {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "employeeId", nullable = false)
    private Long employeeId;

    @Column(name = "projectName", nullable = false, length = 512)
    private String projectName;

    @Column(name = "beginTime", nullable = false, length = 256)
    private String beginTime;

    @Column(name = "endTime", nullable = false, length = 256)
    private String endTime;

    @Column(name = "projectRole", nullable = false, length = 256)
    private String projectRole;

    @Column(name = "experienceDescript", nullable = false, length = 512)
    private String experienceDescript;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDelete", nullable = false)
    private Byte isDelete;

}