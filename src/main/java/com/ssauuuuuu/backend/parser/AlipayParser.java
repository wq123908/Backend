package com.ssauuuuuu.backend.parser;

import com.opencsv.bean.CsvToBeanBuilder;
import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import com.ssauuuuuu.backend.exception.FileParseException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class AlipayParser extends AbstractFileParser {

    private static final String[] ALIPAY_HEADER = {
        "交易时间", "交易分类", "交易对方", "对方账号", "商品说明",
        "收/支", "金额", "收/付款方式", "交易状态", "交易订单号",
        "商家订单号", "备注"
    };

    @Override
    public void validateHeader(InputStream is) throws FileParseException {
        // 保留为空，避免报错
    }

    /**
     * 合并 header 验证与内容解析逻辑，避免重复读取 InputStream
     */
    @Override
    public List<AlipayBillDTO> parseContent(InputStream is) throws FileParseException {

//        try (InputStreamReader reader = new InputStreamReader(is, Charset.forName("GBK"))) {
        // 可以先尝试用 UTF-8 读取
        try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            BufferedReader bufferedReader = new BufferedReader(reader);

            // 跳过前 24 行
            for (int i = 0; i < 24; i++) {
                if (bufferedReader.readLine() == null) {
                    throw new FileParseException("文件格式错误：不足 25 行");
                }
            }

            // 读取第 25 行作为 header
            String headerLine = bufferedReader.readLine();
            if (headerLine == null) {
                throw new FileParseException("文件格式错误：无法找到 header 行");
            }
            System.out.println("支付宝账单文件头::"+headerLine);
            String[] actualHeaders = headerLine.split(",");
            System.out.println("actualHeaders::"+ Arrays.toString(actualHeaders));
            System.out.println("ALIPAY_HEADER::"+ Arrays.toString(ALIPAY_HEADER));
            if (!Arrays.equals(actualHeaders, ALIPAY_HEADER)) {
                log.warn("支付宝账单文件头不完全匹配，预期: {}, 实际: {}",
                         Arrays.toString(ALIPAY_HEADER), Arrays.toString(actualHeaders));
                // 可选：放宽校验，仅记录日志，不抛异常
                // throw new FileParseException("无效的支付宝账单文件头");
            }

            // 继续解析数据（允许字段为空）
            return new CsvToBeanBuilder<AlipayBillDTO>(bufferedReader)
                .withType(AlipayBillDTO.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();

        } catch (Exception e) {
            log.error("文件解析失败", e);
            throw new FileParseException("文件解析失败: " + e.getMessage(), e);
        }
    }
}
