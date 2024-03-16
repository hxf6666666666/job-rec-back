package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "userName", nullable = false, length = 256)
    private String userName;

    @Column(name = "userPassword", nullable = false, length = 512)
    private String userPassword;

    @Column(name = "userNickname", length = 256)
    private String userNickname;

    @Column(name = "userAvatar", length = 1024)
    private String userAvatar;

    @Column(name = "userRole", nullable = false, length = 256)
    private String userRole;

    @Column(name = "createTime", nullable = false)
    private Instant createTime;

    @Column(name = "updateTime", nullable = false)
    private Instant updateTime;

    @Column(name = "isDisabled", nullable = false)
    private Byte isDisabled;

}