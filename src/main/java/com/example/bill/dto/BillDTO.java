package com.example.bill.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillDTO {
    @NotBlank(message = "交易类型不能为空")
    private String transactionType;
    
    @Positive(message = "金额必须大于0")
    @Digits(integer=10, fraction=2, message = "金额格式错误")
    private BigDecimal amount;
    
    @FutureOrPresent(message = "交易时间不能是过去时间")
    private LocalDateTime transactionTime;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
}