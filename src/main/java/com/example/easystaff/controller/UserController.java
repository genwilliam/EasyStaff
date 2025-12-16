package com.example.easystaff.controller;

import com.example.easystaff.domain.User;
import com.example.easystaff.dto.ApiResponse;
import com.example.easystaff.dto.LoginRequest;
import com.example.easystaff.dto.PageResult;
import com.example.easystaff.dto.RegisterRequest;
import com.example.easystaff.dto.ResetPasswordRequest;
import com.example.easystaff.dto.UpdateUserRoleRequest;
import com.example.easystaff.dto.UserQueryRequest;
import com.example.easystaff.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
     * 管理员：用户列表
     */
    @GetMapping("/users")
    public ApiResponse<PageResult<User>> listUsers(@Valid UserQueryRequest request, HttpSession session) {
        User operator = (User) session.getAttribute(SESSION_USER_KEY);
        if (operator == null || !"ADMIN".equalsIgnoreCase(operator.getRole())) {
            return ApiResponse.error("仅管理员可查看用户列表");
        }
        PageResult<User> page = userService.listUsers(request);
        return ApiResponse.success(page);
    }

    /**
     * 管理员：重置密码
     */
    @PostMapping("/users/{id}/reset-password")
    public ApiResponse<Void> resetPassword(@PathVariable("id") Long id, @Valid @RequestBody ResetPasswordRequest request, HttpSession session) {
        User operator = (User) session.getAttribute(SESSION_USER_KEY);
        boolean ok = userService.resetPassword(id, request.getNewPassword(), operator);
        return ok ? ApiResponse.success("重置成功") : ApiResponse.error("重置失败或无权限");
    }

    /**
     * 管理员：调整角色
     */
    @PostMapping("/users/{id}/role")
    public ApiResponse<Void> updateRole(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRoleRequest request, HttpSession session) {
        User operator = (User) session.getAttribute(SESSION_USER_KEY);
        boolean ok = userService.updateRole(id, request.getRole(), operator);
        return ok ? ApiResponse.success("更新成功") : ApiResponse.error("更新失败或无权限");
    }

    /**
     * 管理员：直接创建用户（管理员或普通用户）
     */
    @PostMapping("/users")
    public ApiResponse<User> createUser(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        User operator = (User) session.getAttribute(SESSION_USER_KEY);
        User created = userService.register(request.getUsername(), request.getPassword(), request.getNickname(), request.getRole(), operator);
        if (created == null) {
            return ApiResponse.error("创建失败，检查用户名是否重复或权限不足");
        }
        return ApiResponse.success(created);
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


