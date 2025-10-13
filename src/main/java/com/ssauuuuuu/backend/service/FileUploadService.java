package com.ssauuuuuu.backend.service;

import com.ssauuuuuu.backend.exception.FileParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface FileUploadService {
    Map<String, Object> convertAlipayCSV(MultipartFile file) throws IOException, FileParseException;
}
