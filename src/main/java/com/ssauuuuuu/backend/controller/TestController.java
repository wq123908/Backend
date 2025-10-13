package com.ssauuuuuu.backend.controller;

import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Api Test", description = "测试接口")
public class TestController {

    @PostMapping(value = "/hello")
    public String uploadFile() {
        System.out.println("Hello World!");
        return "Hello World!";
    }

}
