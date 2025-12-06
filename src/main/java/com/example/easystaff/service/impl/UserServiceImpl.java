package com.example.easystaff.service.impl;

import com.example.easystaff.domain.User;
import com.example.easystaff.mapper.UserMapper;
import com.example.easystaff.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        // 简单的明文密码比对（教学示例）
        if (!password.equals(user.getPassword())) {
            return null;
        }
        return user;
    }
}


