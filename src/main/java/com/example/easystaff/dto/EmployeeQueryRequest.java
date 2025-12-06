package com.example.easystaff.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 员工分页 + 条件查询请求参数
 */
@Data
public class EmployeeQueryRequest {

    /**
     * 当前页码，从 1 开始
     */
    private Integer page = 1;

    /**
     * 每页条数，固定为 4
     */
    private Integer pageSize = 4;

    /**
     * 员工姓名（模糊查询）
     */
    private String name;

    /**
     * 职位
     */
    private String position;

    /**
     * 入职开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 入职结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}


