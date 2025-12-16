package com.example.easystaff.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 管理员调整用户角色
 */
@Data
public class UpdateUserRoleRequest {

    @NotBlank(message = "角色不能为空")
    private String role;
}


