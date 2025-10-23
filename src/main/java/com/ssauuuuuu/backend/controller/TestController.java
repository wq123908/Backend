package com.ssauuuuuu.backend.controller;

import com.ssauuuuuu.backend.common.Response;
import com.ssauuuuuu.backend.common.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Api Test", description = "测试接口")
public class TestController {

    @PostMapping(value = "/upload")
    @Operation(summary = "测试文件上传",
        responses = {
            @ApiResponse(responseCode = "200", description = "上传成功",
                content = @Content(schema = @Schema(implementation = Response.class)))
        })
    public ResponseEntity<Response<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseUtil.success("上传成功", "File uploaded: " + file.getOriginalFilename());
    }

}
