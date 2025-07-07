package com.example.bill.parser;

import com.example.bill.exception.UnsupportedFileTypeException;
import com.example.bill.exception.WechatParser;
import org.springframework.stereotype.Component;

@Component
public class FileParserFactory {
    public Object getParser(String fileType) {
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