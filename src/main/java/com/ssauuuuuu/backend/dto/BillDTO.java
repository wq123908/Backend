package com.ssauuuuuu.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BillDTO {

    // 账单唯一标识
    private Integer billId;

    // 用户ID，不能为空
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    // 账单来源类型ID，不能为空
    @NotNull(message = "账单来源类型ID不能为空")
    private Integer sourceTypeId;

    // 账单分类ID，不能为空
    @NotNull(message = "账单分类ID不能为空")
    private Integer categoryId;

    // 交易金额（正数），不能为空且必须大于0
    @NotNull(message = "交易金额不能为空")
    @DecimalMin(value = "0.00", message = "交易金额必须大于等于0")
    private BigDecimal amount;

    // 交易发生时间，不能为空
    @NotNull(message = "交易发生时间不能为空")
    private LocalDateTime transactionTime;

    // 订单金额（商品原价）
    private BigDecimal orderAmount;

    // 优惠券/代金券金额，默认为0.00
    private BigDecimal voucherAmount = BigDecimal.ZERO;

    // 收支类型：1-支出 2-收入，不能为空且必须为1或2
    @NotNull(message = "收支类型不能为空")
    @Min(value = 1, message = "收支类型必须为1或2")
    @Max(value = 2, message = "收支类型必须为1或2")
    private Integer payType;

    // 账单备注/商品名称
    private String remark;

    // 第三方订单号
    private String thirdPartyOrderNo;

    // 商户订单号
    private String merchantOrderNo;

    // 记录创建时间
    private LocalDateTime createTime;

    public BillDTO(Integer billId,
                   Integer userId,
                   Integer sourceTypeId,
                   Integer categoryId,
                   BigDecimal amount,
                   LocalDateTime transactionTime,
                   BigDecimal orderAmount,
                   BigDecimal voucherAmount,
                   Integer payType,
                   String remark,
                   String thirdPartyOrderNo,
                   String merchantOrderNo,
                   LocalDateTime createTime) {

        this.billId = billId;
        this.userId = userId;
        this.sourceTypeId = sourceTypeId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.orderAmount = orderAmount;
        this.voucherAmount = voucherAmount;
        this.payType = payType;
        this.remark = remark;
        this.thirdPartyOrderNo = thirdPartyOrderNo;
        this.merchantOrderNo = merchantOrderNo;
        this.createTime = createTime;
    }

    // 在 BillDTO.java 文件中添加
    public static class WeeklySummary {
        private String week;
        private BigDecimal amount;

        public WeeklySummary(String week, BigDecimal amount) {
            this.week = week;
            this.amount = amount;
        }

        // getters and setters
        public String getWeek() { return week; }
        public void setWeek(String week) { this.week = week; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }
}