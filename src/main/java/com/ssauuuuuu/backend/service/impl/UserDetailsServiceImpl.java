package com.ssauuuuuu.backend.service.impl;

import com.ssauuuuuu.backend.model.User;
import com.ssauuuuuu.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        System.out.println("User: " + user);

        // 创建Spring Security的UserDetails对象，包含用户名、密码和权限
        // 由于数据库没有role字段，给所有用户设置默认角色为USER
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // 密码已经加密，直接使用
                .authorities(Collections.singleton(new SimpleGrantedAuthority("USER")))
                .build();
    }
}