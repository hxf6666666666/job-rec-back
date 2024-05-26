create database if not exists career;

use career;

-- 用户表
create table if not exists user
(
    id        bigint auto_increment comment 'id' primary key,
    userName  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userNickname varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'employee'        not null comment '用户角色：employee/employer/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDisabled     tinyint      default 0                 not null comment '是否禁用',
    index idx_userAccount (userName)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 招聘信息表
create table if not exists recruitment
(
    id              bigint auto_increment comment 'id' primary key,
    jobName         varchar(512)                            not null comment '岗位招聘标题',
    userId          bigint                                  not null comment '岗位发布者id',
    companyName     VARCHAR(512)                            not null comment '公司名称',
    industryId      bigint                                  not null comment '行业id',
    jobDescription  text                                    null comment '职位详情',
    educationType   tinyint                                 null comment '最低学历要求',
    jobPersonality  varchar(1024) default '[]'              not null comment '素养关键词JSON',
    jobSkills       varchar(1024) default '[]'              null comment '职业技术栈JSON',
    jobType         int                                     not null comment '职业类型（实习、全职）',
    jobAddress      varchar(256)                            not null comment '岗位地址',
    salaryUpper     int                                     null comment '薪水上限',
    salaryLower     int                                     null comment '薪水下限',
    salaryUnit      tinyint                                 null comment '薪水种类',
    cityId          bigint                                  not null comment '所在城市id',
    jobActive       tinyint       default 0                 not null comment '招聘活跃 （0 - 招聘中 1 - 结束招聘）',
    createTime      datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint       default 0                 not null comment '是否删除',
    index idx_userId (userId),
    index idx_jobId (id)
) comment '招聘信息' collate = utf8mb4_unicode_ci;

-- 招聘信息收藏
create table recruitment_favour
(
    id            bigint auto_increment comment 'id' primary key,
		employeeId    bigint                             not null comment '求职者id',
    jobId         bigint                             not null comment '职位id（题目所给数据的id）',
    createTime    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    index idx_userId (employeeId),
    index idx_jobId (jobId)
) comment '招聘信息收藏' collate = utf8mb4_unicode_ci;

-- 求职者表
create table if not exists employee
(
    id               bigint auto_increment comment 'id' primary key,
		resumePhoto      VARCHAR(1024)                          NULL COMMENT '简历照片',
		biography        varchar(1024)                      null comment '简历地址',
		realName         VARCHAR(50)                        NOT NULL COMMENT '真实姓名',
    gender           tinyint  default 0                 not null comment '性别（0 - 女 1 - 男）',
    age              int      default 20                not null comment '年龄',
		dateOfBirth      DATE                               NOT NULL COMMENT '出生日期',
		cityId           bigint                             not null comment '居住地',
		userPhone        varchar(512)                           null comment '手机',
    email            varchar(512)                           null comment '邮箱',
		qqNumber         VARCHAR(20)                            NULL COMMENT 'QQ号',
    wechat           VARCHAR(50)                            NULL COMMENT '微信号',
		skillTag         varchar(512)                       not null comment '技能标签',
		languageTag      VARCHAR(512)                           NULL COMMENT '语言标签',
    competencyTag    VARCHAR(512)                           NULL COMMENT '素养标签',
		advantage        text                               null comment '个人优势',
    qualificationIds varchar(1024)                      null comment '获奖证书',
		education        int      default 1                 not null comment '最高学历',
    graduateYear     int                                not null comment '毕业年份',
		schoolName       varchar(256)                       not null comment '学校名称',
		majorName        varchar(256)                       not null comment '专业名称',
		GPA              DECIMAL(3,2)                       NULL COMMENT 'GPA',
    ranking          INT                                NULL COMMENT '排名',
		workExperienceYear INT DEFAULT 0                    NOT NULL COMMENT '工作经验年限',
    jobStatus        tinyint                            not null comment '求职状态',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除',
    index idx_id (id)
) comment '应聘者' collate = utf8mb4_unicode_ci;


-- 求职者信息收藏
CREATE TABLE IF NOT EXISTS talent_favour
(
    id            BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
		userId        BIGINT                             NOT NULL COMMENT '用户id',
    employeeId    bigint                             not null comment '求职者id',
    createTime    DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    updateTime    DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_userId (userId),
    INDEX idx_employeeId (employeeId)
) COMMENT '求职者信息收藏' COLLATE = utf8mb4_unicode_ci;


-- 求职者工作实习经历表
create table if not exists employee_job_experience
(
    id                 bigint auto_increment comment 'id' primary key,
    employeeId           bigint                             not null comment '求职者id',
    companyName        VARCHAR(512)                       not null comment '公司名称',
    beginTime          varchar(256)                       not null comment '开始时间',
    endTime            varchar(256)                       not null comment '结束时间',
    jobRole            varchar(256)                       not null comment '担任职务',
    experienceDescript varchar(512)                       not null comment '经历描述',
    createTime         datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete           tinyint  default 0                 not null comment '是否删除',
    index idx_employeeId (employeeId)
) comment '求职者工作实习经历';

-- 求职者项目经历表
CREATE TABLE IF NOT EXISTS project_experience
(
    id                 BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    employeeId         bigint                             not null comment '求职者id',
    projectName        VARCHAR(512)                       NOT NULL COMMENT '项目名称',
    beginTime          VARCHAR(256)                       NOT NULL COMMENT '开始时间',
    endTime            VARCHAR(256)                       NOT NULL COMMENT '结束时间',
    projectRole        VARCHAR(256)                       NOT NULL COMMENT '项目角色',
    experienceDescript VARCHAR(512)                       NOT NULL COMMENT '项目描述',
    createTime         DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime         DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete           TINYINT DEFAULT 0                 NOT NULL COMMENT '是否删除',
    INDEX idx_employeeId (employeeId)
) COMMENT '求职者项目经历';

-- 教育经历表
create table if not exists education_experience
(
    id            bigint auto_increment comment 'id' primary key,
    employeeId    bigint                             not null comment '求职者id',
    schoolName    varchar(256)                       not null comment '学校名称',
    educationType tinyint                            not null comment '学历类型',
    beginYear     int                                not null comment '开始年份',
    endYear       int                                not null comment '结束年份',
    majorName     varchar(256)                       not null comment '专业名称',
    activity      text                               null comment '在校经历',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除',
    index idx_employeeId (employeeId)
) comment '教育经历' collate = utf8mb4_unicode_ci;

-- 行业表
create table if not exists industry
(
    id               bigint auto_increment                       comment 'id' primary key,
    industryName     varchar(256)                       not null comment '行业名称',
		skillTag         varchar(512)                       not null comment '技能标签',
		languageTag      VARCHAR(512)                           NULL COMMENT '语言标签',
    competencyTag    VARCHAR(512)                           NULL COMMENT '素养标签',
    MajorTag         VARCHAR(512)                           NULL COMMENT '专业标签',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '行业' collate = utf8mb4_unicode_ci;

-- 求职者期望岗位
create table if not exists employee_wish_career
(
    id                bigint auto_increment comment 'id' primary key,
    userId            bigint                                 not null comment '用户id',
    industryId        bigint       default 0                 not null comment '行业id',
    cityId            bigint                                 not null comment '工作城市',
		jobType           int                                    not null comment '职业类型（实习、全职）',
    salaryUpper       int                                    null comment '薪水上限',
    salaryLower       int                                    null comment '薪水下限',
    createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint      default 0                 not null comment '是否删除'
)COMMENT '期望岗位' COLLATE = utf8mb4_unicode_ci;

-- 城市信息表
CREATE TABLE IF NOT EXISTS city (
    id            BIGINT AUTO_INCREMENT COMMENT '城市ID' PRIMARY KEY,
    cityName      VARCHAR(256) NOT NULL COMMENT '城市名称',
    provinceName  VARCHAR(256) NOT NULL COMMENT '所在省份名称',
    postalCode    VARCHAR(20)  NOT NULL COMMENT '邮政编码',
    createTime    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    isDeleted     TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除，0表示未删除，1表示已删除',
    UNIQUE(cityName, provinceName) COMMENT '唯一索引：城市名称和所在省份的组合唯一'
) COMMENT '城市信息表' COLLATE = utf8mb4_unicode_ci;

-- 用户浏览历史表
CREATE TABLE IF NOT EXISTS job_history(
    id          bigint auto_increment               comment 'id' primary key,
    userId      bigint                              not null comment '用户ID',
    recruitmentId bigint                            not null comment '职位ID',
    createTime    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    FOREIGN KEY (userId) REFERENCES user(id),
    FOREIGN KEY (recruitmentId) REFERENCES recruitment(id)
);

-- 用户收藏职位表
CREATE TABLE IF NOT EXISTS job_favorites (
    id          bigint auto_increment               comment 'id' primary key,
    userId      bigint                              not null comment '用户ID',
    recruitmentId bigint                            not null comment '职位ID',
    createTime    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    FOREIGN KEY (userId) REFERENCES user(id),
    FOREIGN KEY (recruitmentId) REFERENCES recruitment(id)
);

-- 投递信息表
CREATE TABLE IF NOT EXISTS job_applications (
    id          bigint auto_increment               comment 'id' primary key,
    userId      bigint                              not null comment '用户ID',
    recruitmentId bigint                            not null comment '职位ID',
    offerStatus      varchar(50)                        not null comment '投递状态',
    createTime    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    FOREIGN KEY (userId) REFERENCES user(id),
    FOREIGN KEY (recruitmentId) REFERENCES recruitment(id)
);