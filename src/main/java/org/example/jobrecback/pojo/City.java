package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cityName", nullable = false, length = 256)
    private String cityName;

    @Column(name = "provinceName", nullable = false, length = 256)
    private String provinceName;

    @Column(name = "postalCode", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDeleted", nullable = false)
    private Byte isDeleted;

}