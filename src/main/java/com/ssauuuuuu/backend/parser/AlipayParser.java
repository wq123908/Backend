package com.ssauuuuuu.backend.parser;

import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import com.ssauuuuuu.backend.exception.FileParseException;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class AlipayParser extends AbstractFileParser {
    private static final String[] ALIPAY_HEADER = {
        "交易号", "商家订单号", "交易创建时间", "付款时间", "最近修改时间", 
        "交易来源地", "类型", "交易对方", "商品名称", "金额（元）", "收/支", "交易状态"
    };

    @Override
    public void validateHeader(InputStream is) throws FileParseException {
        try (InputStreamReader reader = new InputStreamReader(is)) {
            String firstLine = new BufferedReader(reader).readLine();
            if (!Arrays.equals(firstLine.split(","), ALIPAY_HEADER)) {
                throw new FileParseException("无效的支付宝账单文件头");
            }
        } catch (IOException e) {
            throw new FileParseException("文件读取失败", e);
        }
    }

    @Override
    public List<AlipayBillDTO> parseContent(InputStream is) throws FileParseException {
        return new CsvToBeanBuilder<AlipayBillDTO>(new InputStreamReader(is))
                .withType(AlipayBillDTO.class)
                .build()
                .parse();
    }


}