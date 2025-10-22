package com.ssauuuuuu.backend.config;

import com.ssauuuuuu.backend.model.User;
import com.ssauuuuuu.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有用户数据
        if (!userRepository.existsByUsername("test")) {
            // 创建普通用户
            User user = new User();
            user.setUsername("test");
            user.setPassword(passwordEncoder.encode("test123")); // 加密密码
            user.setEmail("test@example.com");
            userRepository.save(user);
        }

        if (!userRepository.existsByUsername("admin")) {
            // 创建管理员用户（注：由于数据库没有role字段，在UserDetailsServiceImpl中会设置为默认ROLE_USER角色）
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // 加密密码
            admin.setEmail("admin@example.com");
            userRepository.save(admin);
        }
    }
}