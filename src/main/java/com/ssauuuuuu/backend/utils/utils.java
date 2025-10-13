package com.ssauuuuuu.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.detect.EncodingDetector;
import org.apache.tika.metadata.Metadata;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class utils {
    // 在 `AlipayParser.parseContent` 方法中添加编码检测逻辑
    public static Charset detectCharset(InputStream is) {
        try {
            // 重置流位置（如果可能）
            is.mark(1024);

            // 尝试常见编码
            Charset[] charsets = {StandardCharsets.UTF_8, Charset.forName("GBK"), StandardCharsets.ISO_8859_1};

            for (Charset charset : charsets) {
                try (InputStreamReader reader = new InputStreamReader(is, charset);
                     BufferedReader bufferedReader = new BufferedReader(reader)) {

                    // 读取前几行测试
                    String line = bufferedReader.readLine();
                    if (line != null && !line.contains("")) {  // 检查是否包含乱码字符
                        return charset;
                    }
                } catch (Exception e) {
                    // 继续尝试下一个编码
                }
            }
        } catch (Exception e) {
            log.warn("编码检测失败，使用默认UTF-8编码", e);
        }

        return StandardCharsets.UTF_8; // 默认返回UTF-8
    }

    public static LocalDateTime parseMonthString(String monthStr) {
        try {
            // 处理单独的月份数字（1-12），自动使用当前年份
            if (monthStr.matches("\\d{1,2}")) {
                int monthValue = Integer.parseInt(monthStr);
                if (monthValue >= 1 && monthValue <= 12) {
                    YearMonth yearMonth = YearMonth.now().withMonth(monthValue);
                    return yearMonth.atDay(1).atStartOfDay();
                } else {
                    throw new IllegalArgumentException("Month must be between 1 and 12");
                }
            }
            // 处理 "yyyy-MM" 格式
            else if (monthStr.matches("\\d{4}-\\d{1,2}")) {
                YearMonth yearMonth = YearMonth.parse(monthStr, DateTimeFormatter.ofPattern("yyyy-M"));
                return yearMonth.atDay(1).atStartOfDay();
            }
            // 处理 "yyyyMM" 格式
            else if (monthStr.matches("\\d{6}")) {
                YearMonth yearMonth = YearMonth.parse(monthStr, DateTimeFormatter.ofPattern("yyyyMM"));
                return yearMonth.atDay(1).atStartOfDay();
            }
            // 处理完整日期时间格式
            else if (monthStr.contains(" ") && monthStr.contains(":")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return LocalDateTime.parse(monthStr, formatter);
            }
            // 默认处理 ISO 格式
            else {
                return LocalDateTime.parse(monthStr);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + monthStr + ". Supported formats: yyyy-MM, yyyyMM, yyyy-MM-dd HH:mm, or single month number (1-12)");
        }
    }


}
