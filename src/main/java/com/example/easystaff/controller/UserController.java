package com.example.easystaff.controller;

import com.example.easystaff.domain.User;
import com.example.easystaff.dto.ApiResponse;
import com.example.easystaff.dto.LoginRequest;
import com.example.easystaff.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    public static final String SESSION_USER_KEY = "loginUser";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<User> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ApiResponse.error("用户名或密码错误");
        }
        // 登录成功保存到 session
        session.setAttribute(SESSION_USER_KEY, user);
        return ApiResponse.success(user);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success("已退出登录");
    }
}


