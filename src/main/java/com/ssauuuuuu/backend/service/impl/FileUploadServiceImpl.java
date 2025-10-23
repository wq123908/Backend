package com.ssauuuuuu.backend.service.impl;

import com.ssauuuuuu.backend.convert.FileUploadAlipayConvertBill;
import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import com.ssauuuuuu.backend.dto.BillDTO;
import com.ssauuuuuu.backend.exception.FileParseException;
import com.ssauuuuuu.backend.model.Bill;
import com.ssauuuuuu.backend.parser.FileParserFactory;
import com.ssauuuuuu.backend.service.BillService;
import com.ssauuuuuu.backend.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ssauuuuuu.backend.convert.FileUploadAlipayConvertBill.*;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final BillService billService;

    public FileUploadServiceImpl(BillService billService) {
        this.billService = billService;
    }


    @Override
    public Map<String, Object> convertAlipayCSV(MultipartFile file) throws IOException, FileParseException {
        // 创建上传目录
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        try {
            // 保存文件
            //file.transferTo(filePath);

            // 解析文件
            List<AlipayBillDTO> result = FileParserFactory.getParser("alipay")
                .parseContent(Files.newInputStream(filePath));

            // 数据清洗
            List<Bill> bills = result.stream()
                .map(FileUploadAlipayConvertBill::convertToBillDTO)
                .toList();

            // 持久化
            billService.saveBills(bills);

            // 返回结果
            return Map.of(
                "savedPath", filePath.toString(),
                "records", result
            );
        } catch (FileParseException e) {
            Files.deleteIfExists(filePath);
            throw e;
        }
    }



}
