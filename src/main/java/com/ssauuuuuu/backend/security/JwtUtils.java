package com.ssauuuuuu.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());
    
    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long expirationMs;
    
    private String originalSecretKey; // 保存原始密钥用于验证

    @PostConstruct
    protected void init() {
        originalSecretKey = secretKey; // 保存原始密钥
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 替换原有的 generateToken 方法
    public String generateToken(String username) {
        // 使用 Keys.secretKeyFor 生成符合 HS512 要求的安全密钥
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs * 1000))
                .signWith(key) // 使用生成的安全密钥
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.warning("Invalid JWT signature: {}" + e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warning("Invalid JWT token: {}" + e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.warning("JWT token is expired: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warning("JWT token is unsupported: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warning("JWT claims string is empty: {}" + e.getMessage());
        }
        return false;
    }
    
    // 获取JWT中的用户名
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
    
    // 获取原始密钥（未Base64编码）
    public String getSecretKey() {
        return originalSecretKey;
    }
}
