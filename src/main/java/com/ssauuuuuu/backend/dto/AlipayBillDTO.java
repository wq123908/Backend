package com.ssauuuuuu.backend.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlipayBillDTO {
    @CsvBindByName(column = "交易号")
    private String tradeNo;

    @CsvBindByName(column = "商家订单号")
    private String merchantOrderNo;

    @CsvBindByName(column = "交易创建时间")
    private Date createTime;

    @CsvBindByName(column = "付款时间")
    private Date payTime;

    @CsvBindByName(column = "金额（元）")
    private BigDecimal amount;

    @CsvBindByName(column = "收/支")
    private String type;

    @CsvBindByName(column = "商品名称")
    private String productName;
}