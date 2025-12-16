package com.example.easystaff.dto;

import lombok.Data;

/**
 * 管理端用户查询请求
 */
@Data
public class UserQueryRequest {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 用户名模糊查询
     */
    private String username;

    /**
     * 角色过滤：ADMIN / USER
     */
    private String role;
}


