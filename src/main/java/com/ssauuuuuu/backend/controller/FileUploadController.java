package com.ssauuuuuu.backend.controller;

import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import com.ssauuuuuu.backend.exception.FileParseException;
import com.ssauuuuuu.backend.parser.FileParserFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Upload", description = "账单文件上传接口")
public class FileUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 新增文件保存逻辑：
//    Path uploadPath = Path.of(uploadDir);
//    Files.createDirectories(uploadPath);
//    file.transferTo(filePath);

    @PostMapping(value = "/alipay", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "支付宝账单转换",
        responses = {
            @ApiResponse(responseCode = "200", description = "转换成功",
                content = @Content(schema = @Schema(implementation = AlipayBillDTO[].class))),
            @ApiResponse(responseCode = "400", description = "文件格式错误")
        })
    public ResponseEntity<?> convertAlipayCSV(
        @RequestPart("file") MultipartFile file) throws IOException {
        System.out.println("进入到支付宝账单转换");
        // 创建上传目录
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        // 保存文件
        file.transferTo(filePath);

        try {
            List<AlipayBillDTO> result = FileParserFactory.getParser("alipay")
                .parseContent(Files.newInputStream(filePath));
            
            return ResponseEntity.ok(Map.of(
                "savedPath", filePath.toString(),
                "records", result
            ));
        } catch (FileParseException e) {
            Files.deleteIfExists(filePath);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}