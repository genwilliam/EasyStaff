package com.example.easystaff.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 批量更新员工请求
 */
@Data
public class EmployeeBatchUpdateRequest {

    private List<Long> ids;

    private String department;

    private String position;

    private String employmentStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;
}


