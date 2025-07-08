package com.ssauuuuuu.backend.controller;

//@RestController
//@RequestMapping("/api/auth")
public class AuthController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        // 认证逻辑实现
//        String token = jwtUtils.generateToken(request.getUsername());
//        System.out.println("登录页面");
//        return ResponseEntity.ok(new JwtResponse(token));
//    }

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

//    private static class JwtResponse {
//        private final String token;
//        JwtResponse(String token) { this.token = token; }
//        public String getToken() { return token; }
//    }
}