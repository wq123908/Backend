package com.ssauuuuuu.backend.controller;

import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import com.ssauuuuuu.backend.exception.FileParseException;
import com.ssauuuuuu.backend.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/files")
@Tag(name = "File Upload", description = "账单文件上传接口")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(value = "/alipay", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "支付宝账单转换",
        responses = {
            @ApiResponse(responseCode = "200", description = "转换成功",
                content = @Content(schema = @Schema(implementation = AlipayBillDTO[].class))),
            @ApiResponse(responseCode = "400", description = "文件格式错误")
        })
    public ResponseEntity<?> convertAlipayCSV(@RequestPart("file") MultipartFile file) {
        try {
            Map<String, Object> result = fileUploadService.convertAlipayCSV(file);
            return ResponseEntity.ok(result);
        } catch (FileParseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("文件处理失败");
        }
    }
}
