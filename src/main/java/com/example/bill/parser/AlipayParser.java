package com.example.bill.parser;

import com.example.bill.exception.FileParseException;
import java.io.InputStream;

public class AlipayParser extends AbstractFileParser {
    @Override
    public void validateHeader(InputStream is) throws FileParseException {
        // 支付宝文件头校验逻辑
    }

    @Override
    public void parseContent(InputStream is) throws FileParseException {
        // 支付宝账单解析逻辑
    }
}