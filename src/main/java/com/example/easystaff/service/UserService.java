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
}


