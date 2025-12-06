package com.example.easystaff.service;

import com.example.easystaff.domain.User;

public interface UserService {

    /**
     * 根据用户名和密码执行登录校验，成功返回用户信息，失败返回 null
     */
    User login(String username, String password);
}


