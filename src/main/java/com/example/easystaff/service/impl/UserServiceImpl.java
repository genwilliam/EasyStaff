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

    @Override
    public User register(String username, String password, String nickname, String role, User operator) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        if (userMapper.findByUsername(username) != null) {
            return null;
        }

        String targetRole = "USER";
        if ("ADMIN".equalsIgnoreCase(role)) {
            if (operator != null && "ADMIN".equalsIgnoreCase(operator.getRole())) {
                targetRole = "ADMIN";
            }
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setNickname(StringUtils.hasText(nickname) ? nickname : username);
        newUser.setRole(targetRole);

        userMapper.insert(newUser);
        return newUser;
    }

    @Override
    public com.example.easystaff.dto.PageResult<User> listUsers(com.example.easystaff.dto.UserQueryRequest request) {
        int page = request.getPage() == null || request.getPage() < 1 ? 1 : request.getPage();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? 10 : request.getPageSize();
        pageSize = Math.min(pageSize, 100);

        String username = request.getUsername();
        String role = request.getRole();

        long total = userMapper.count(username, role);
        int offset = (page - 1) * pageSize;
        java.util.List<User> list = userMapper.findPage(username, role, offset, pageSize);

        com.example.easystaff.dto.PageResult<User> pageResult = new com.example.easystaff.dto.PageResult<>();
        pageResult.setList(list);
        pageResult.setTotal(total);
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
        return pageResult;
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword, User operator) {
        if (operator == null || !"ADMIN".equalsIgnoreCase(operator.getRole())) {
            return false;
        }
        if (!StringUtils.hasText(newPassword) || userId == null) {
            return false;
        }
        return userMapper.updatePassword(userId, newPassword) > 0;
    }

    @Override
    public boolean updateRole(Long userId, String role, User operator) {
        if (operator == null || !"ADMIN".equalsIgnoreCase(operator.getRole())) {
            return false;
        }
        if (!StringUtils.hasText(role) || userId == null) {
            return false;
        }
        String upper = role.toUpperCase();
        if (!"ADMIN".equals(upper) && !"USER".equals(upper)) {
            return false;
        }
        return userMapper.updateRole(userId, upper) > 0;
    }
}


