package com.ssauuuuuu.backend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.web.SecurityFilterChain;


public class SecurityConfig{

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .securityMatchers(matchers -> matchers
                .requestMatchers("/api/**")
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/files/alipay", "/api/files/alipay/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults())
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
            );

        return http.build();
    }


    @Bean
    InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user = User.builder()
            .username("test")
            .password("{noop}test123")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

}

