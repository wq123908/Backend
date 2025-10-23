package com.ssauuuuuu.backend.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应类
 * 用于封装所有API响应，包含状态码、消息和数据
 */
public class Response<T> {
    private String code;
    private String message;
    private T data;

    public Response() {
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> Response<T> success(T data) {
        return new Response<>("success", "操作成功", data);
    }

    /**
     * 成功响应（带自定义消息）
     */
    public static <T> Response<T> success(String message, T data) {
        return new Response<>("success", message, data);
    }

    /**
     * 失败响应
     */
    public static <T> Response<T> failed(String message) {
        return new Response<>("failed", message, null);
    }

    @Override
    public String toString() {
        return "Response{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}