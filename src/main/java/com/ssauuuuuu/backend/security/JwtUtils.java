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
    
    private SecretKey signingKey; // 使用SecretKey对象而不是String

    @PostConstruct
    protected void init() {
        // 确保密钥长度至少为512位（64字节）
        // 如果配置的密钥太短，使用Keys.secretKeyFor生成足够长度的密钥
        if (secretKey.length() < 64) {
            logger.warning("配置的JWT密钥长度不足，生成安全的随机密钥");
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        } else {
            // 使用配置的密钥，但确保它被正确处理
            signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs * 1000))
                .signWith(signingKey, SignatureAlgorithm.HS512) // 明确指定算法
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.warning("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warning("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.warning("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warning("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warning("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
    
    // 获取JWT中的用户名
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
    
    // 获取签名密钥
    public SecretKey getSigningKey() {
        return signingKey;
    }
}
