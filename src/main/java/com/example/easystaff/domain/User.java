package com.example.easystaff.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
public class User {

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（简单起见明文存储，真实项目应加密）
     */
    private String password;

    /**
     * 昵称或姓名
     */
    private String nickname;

    /**
     * 角色：ADMIN / USER
     */
    private String role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}


