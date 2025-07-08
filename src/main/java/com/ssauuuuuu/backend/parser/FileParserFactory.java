package com.ssauuuuuu.backend.parser;

import com.ssauuuuuu.backend.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Component;

@Component
public class FileParserFactory {
   public static AlipayParser getParser(String fileType) {
    if (fileType == null || fileType.trim().isEmpty()) {
        throw new UnsupportedFileTypeException("File type cannot be null or blank.");
    }

    String type = fileType.toLowerCase();

    return switch (type) {
        case "alipay" -> new AlipayParser();
        //case "wechat" -> new WechatParser();
        default -> throw new UnsupportedFileTypeException("Unsupported file type: " + type);
    };
}

}