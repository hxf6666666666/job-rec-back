package org.example.jobrecback.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/*
 * 用户表
 * 用于注册、登录、用户管理
 */

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    // 用户名（登录账号）
    private Long id;
    @Column(name = "userName", nullable = false, length = 256)
    private String userName;
    // 密码
    @Column(name = "userPassword", nullable = false, length = 512)
    private String userPassword;
    // 昵称
    @Column(name = "userNickname", length = 256)
    private String userNickname;
    // 头像(在线链接)
    @Column(name = "userAvatar", length = 1024)
    private String userAvatar;
    // 角色：超级管理员、管理员、求职者、招聘者
    @Column(name = "userRole", nullable = false, length = 256)
    private String userRole;
    // 创建时间
    @Column(name = "createTime")
    private Instant createTime;
    // 更新时间
    @Column(name = "updateTime")
    private Instant updateTime;
    // 是否禁用
    @Column(name = "isDisabled")
    private Byte isDisabled;

}