package com.ssauuuuuu.backend.controller;

import com.ssauuuuuu.backend.common.Response;
import com.ssauuuuuu.backend.common.ResponseUtil;
import com.ssauuuuuu.backend.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录",
        responses = {
            @ApiResponse(responseCode = "200", description = "登录成功",
                content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "用户名或密码错误",
                content = @Content(schema = @Schema(implementation = Response.class)))
        })
    public ResponseEntity<Response<JwtResponse>> login(@RequestBody LoginRequest request) {
        try {
            // 使用AuthenticationManager进行认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // 设置认证上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 生成JWT token
            String token = jwtUtils.generateToken(request.getUsername());

            // 返回成功响应
            return ResponseUtil.success("登录成功", new JwtResponse(token));
        } catch (AuthenticationException e) {
            // 认证失败，返回错误信息
            return ResponseUtil.unauthorized("用户名或密码错误");
        }
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "未授权",
                content = @Content(schema = @Schema(implementation = Response.class)))
        })
    public ResponseEntity<Response<UserInfoResponse>> getUserInfo(Authentication authentication) {
        if (authentication != null) {
            return ResponseUtil.success("获取成功", new UserInfoResponse(
                authentication.getName(),
                authentication.getAuthorities().toString()
            ));
        }
        return ResponseUtil.unauthorized("未授权");
    }

    // 内部DTO类
    private static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private static class JwtResponse {
        private final String token;
        private final String tokenType = "Bearer";
        
        JwtResponse(String token) { 
            this.token = token; 
        }
        
        public String getToken() { 
            return token; 
        }
        
        public String getTokenType() {
            return tokenType;
        }
    }
    
    private static class ErrorResponse {
        private final String message;
        
        ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    private static class UserInfoResponse {
        private final String username;
        private final String authorities;
        
        UserInfoResponse(String username, String authorities) {
            this.username = username;
            this.authorities = authorities;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getAuthorities() {
            return authorities;
        }
    }
}