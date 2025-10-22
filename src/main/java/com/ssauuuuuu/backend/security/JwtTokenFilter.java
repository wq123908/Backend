package com.ssauuuuuu.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    
    @Autowired
    public JwtTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response,
                                  FilterChain filterChain)
        throws ServletException, IOException {
        try {
            // 获取Authorization头
            String header = request.getHeader("Authorization");
            String token = null;
            String username = null;
            
            // 检查Authorization头格式
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                
                try {
                    // 解析JWT token获取用户名
                    Claims claims = Jwts.parser()
                            .setSigningKey(Base64.getEncoder().encodeToString(jwtUtils.getSecretKey().getBytes()))
                            .parseClaimsJws(token)
                            .getBody();
                    
                    username = claims.getSubject();
                } catch (Exception e) {
                    // Token无效
                    logger.error("JWT token无效: " + e.getMessage());
                }
            }
            
            // 如果token有效且用户未被认证
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 从UserDetailsService获取用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 创建认证token
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 设置认证上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("无法设置用户认证: " + e.getMessage());
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
}