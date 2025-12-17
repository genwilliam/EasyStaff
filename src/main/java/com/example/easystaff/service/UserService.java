package com.example.easystaff.service;

import com.example.easystaff.domain.User;

public interface UserService {

    /**
     * 根据用户名和密码执行登录校验，成功返回用户信息，失败返回 null
     */
    User login(String username, String password);

    /**
     * 注册用户，返回创建成功的用户；如用户名重复返回 null
     * 非管理员只能创建 USER；管理员可指定 ADMIN/USER
     */
    User register(String username, String password, String nickname, String role, User operator);

    /**
     * 管理员分页查询用户
     */
    com.example.easystaff.dto.PageResult<User> listUsers(com.example.easystaff.dto.UserQueryRequest request);

    /**
     * 管理员重置密码
     */
    boolean resetPassword(Long userId, String newPassword, User operator);

    /**
     * 管理员调整角色
     */
    boolean updateRole(Long userId, String role, User operator);

    /**
     * 删除用户（仅超级管理员 admin 允许）
     */
    boolean deleteUser(Long userId, User operator);
}


