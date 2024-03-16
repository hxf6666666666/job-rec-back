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
@Table(name = "industry")
public class Industry {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "industryName", nullable = false, length = 256)
    private String industryName;

    @Column(name = "skillTag", nullable = false, length = 512)
    private String skillTag;

    @Column(name = "languageTag", length = 512)
    private String languageTag;

    @Column(name = "competencyTag", length = 512)
    private String competencyTag;

    @Column(name = "MajorTag", length = 512)
    private String majorTag;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDelete", nullable = false)
    private Byte isDelete;

}