package com.example.easystaff.dto;

import lombok.Data;

import java.util.List;

/**
 * 通用分页结果封装
 */
@Data
public class PageResult<T> {

    private List<T> list;

    private long total;

    private int page;

    private int pageSize;
}


