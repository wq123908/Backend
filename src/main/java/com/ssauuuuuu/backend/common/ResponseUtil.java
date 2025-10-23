package com.ssauuuuuu.backend.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 响应工具类
 * 用于简化ResponseEntity的创建
 */
public class ResponseUtil {

    /**
     * 成功响应
     */
    public static <T> ResponseEntity<Response<T>> success(T data) {
        return ResponseEntity.ok(Response.success(data));
    }

    /**
     * 成功响应（带自定义消息）
     */
    public static <T> ResponseEntity<Response<T>> success(String message, T data) {
        return ResponseEntity.ok(Response.success(message, data));
    }

    /**
     * 失败响应（默认400状态码）
     */
    public static <T> ResponseEntity<Response<T>> failed(String message) {
        return ResponseEntity.badRequest().body(Response.failed(message));
    }

    /**
     * 失败响应（带自定义状态码）
     */
    public static <T> ResponseEntity<Response<T>> failed(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Response.failed(message));
    }

    /**
     * 未授权响应
     */
    public static <T> ResponseEntity<Response<T>> unauthorized(String message) {
        return failed(HttpStatus.UNAUTHORIZED, message);
    }

    /**
     * 禁止访问响应
     */
    public static <T> ResponseEntity<Response<T>> forbidden(String message) {
        return failed(HttpStatus.FORBIDDEN, message);
    }

    /**
     * 未找到资源响应
     */
    public static <T> ResponseEntity<Response<T>> notFound(String message) {
        return failed(HttpStatus.NOT_FOUND, message);
    }
}