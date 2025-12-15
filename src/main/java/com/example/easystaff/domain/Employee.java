package com.example.easystaff.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
public class Employee {

    private Long id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 职位
     */
    private String position;

    /**
     * 在职状态：ACTIVE / INACTIVE
     */
    private String employmentStatus;

    /**
     * 部门
     */
    private String department;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}


