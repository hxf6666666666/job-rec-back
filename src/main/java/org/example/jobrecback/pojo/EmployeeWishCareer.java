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
@Table(name = "employee_wish_career")
public class EmployeeWishCareer {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "industryId", nullable = false)
    private Long industryId;

    @Column(name = "cityId", nullable = false)
    private Long cityId;

    @Column(name = "jobType", nullable = false)
    private Integer jobType;

    @Column(name = "salaryUpper")
    private Integer salaryUpper;

    @Column(name = "salaryLower")
    private Integer salaryLower;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDelete", nullable = false)
    private Byte isDelete;

}