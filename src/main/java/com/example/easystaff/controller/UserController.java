package com.example.easystaff.controller;

import com.example.easystaff.domain.User;
import com.example.easystaff.dto.ApiResponse;
import com.example.easystaff.dto.LoginRequest;
import com.example.easystaff.dto.RegisterRequest;
import com.example.easystaff.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

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
     * 当前登录用户信息
     */
    @GetMapping("/current-user")
    public ApiResponse<User> currentUser(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER_KEY);
        if (user == null) {
            return ApiResponse.error("未登录");
        }
        return ApiResponse.success(user);
    }

    /**
     * 用户注册（默认普通用户）；仅管理员可创建管理员
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        User operator = (User) session.getAttribute(SESSION_USER_KEY);
        User created = userService.register(request.getUsername(), request.getPassword(), request.getNickname(), request.getRole(), operator);
        if (created == null) {
            return ApiResponse.error("注册失败，可能是用户名已存在或参数无效");
        }
        return ApiResponse.success(created);
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


