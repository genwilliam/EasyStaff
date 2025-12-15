package com.example.easystaff.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String nickname;

    /**
     * 角色：ADMIN/USER，默认USER；仅管理员可指定ADMIN
     */
    private String role;
}

