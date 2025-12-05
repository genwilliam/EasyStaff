-- 数据库：请先确保已创建数据库 easystaff（或按需修改）
CREATE DATABASE easystaff CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE easystaff;

-- =========================
-- 用户表 user
-- =========================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`     VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`     VARCHAR(100) NOT NULL COMMENT '登录密码（示例中为明文）',
    `nickname`     VARCHAR(50)           DEFAULT NULL COMMENT '昵称/姓名',
    `create_time`  DATETIME              DEFAULT NULL COMMENT '创建时间',
    `update_time`  DATETIME              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 初始化一个管理员账户：用户名 admin / 密码 123456
INSERT INTO `user` (`username`, `password`, `nickname`, `create_time`, `update_time`)
VALUES ('admin', '123456', '管理员', NOW(), NOW());


-- =========================
-- 员工表 employee
-- =========================
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        VARCHAR(50)  NOT NULL COMMENT '员工姓名',
    `age`         INT                   DEFAULT NULL COMMENT '年龄',
    `position`    VARCHAR(50)           DEFAULT NULL COMMENT '职位',
    `entry_date`  DATE                  DEFAULT NULL COMMENT '入职日期',
    `create_time` DATETIME              DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

-- 初始化几条员工数据，方便调试分页与查询
INSERT INTO `employee` (`name`, `age`, `position`, `entry_date`, `create_time`, `update_time`) VALUES
('张三', 28, '开发工程师', '2025-01-05', NOW(), NOW()),
('李四', 32, '测试工程师', '2025-02-10', NOW(), NOW()),
('王五', 26, '产品经理', '2025-03-15', NOW(), NOW()),
('赵六', 30, '开发工程师', '2025-04-01', NOW(), NOW()),
('钱七', 29, '运维工程师', '2025-05-20', NOW(), NOW()),
('孙八', 35, '架构师',   '2025-06-18', NOW(), NOW()),
('周九', 24, '实习生',   '2025-07-22', NOW(), NOW());


