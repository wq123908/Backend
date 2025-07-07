package com.example.bill.parser;

import com.example.bill.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Component;

@Component
public class FileParserFactory {
    public AbstractFileParser getParser(String fileType) {
        switch(fileType.toLowerCase()) {
            case "alipay":
                return new AlipayParser();
            case "wechat":
                return new WechatParser();
            default:
                throw new UnsupportedFileTypeException("Unsupported file type: " + fileType);
        }
    }
}