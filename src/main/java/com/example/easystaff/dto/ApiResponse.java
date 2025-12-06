package com.example.easystaff.dto;

import lombok.Data;

/**
 * 接口统一返回结构
 */
@Data
public class ApiResponse<T> {

    private int code;

    private String msg;

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(0);
        resp.setMsg("success");
        resp.setData(data);
        return resp;
    }

    public static <T> ApiResponse<T> success(String msg) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(0);
        resp.setMsg(msg);
        return resp;
    }

    public static <T> ApiResponse<T> error(String msg) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(1);
        resp.setMsg(msg);
        return resp;
    }
}


