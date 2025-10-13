package com.ssauuuuuu.backend.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class AlipayBillDTO {

    @CsvBindByName(column = "交易时间", required = true)
    @CsvBindByPosition(position = 0)
    private String transactionTime;

    @CsvBindByName(column = "交易分类", required = true)
    @CsvBindByPosition(position = 1)
    private String transactionCategory;

    @CsvBindByName(column = "交易对方", required = true)
    @CsvBindByPosition(position = 2)
    private String transactionCounterparty;

    @CsvBindByName(column = "对方账号", required = true)
    @CsvBindByPosition(position = 3)
    private String counterpartyAccount;

    @CsvBindByName(column = "商品说明", required = true)
    @CsvBindByPosition(position = 4)
    private String productDescription;

    @CsvBindByName(column = "收/支", required = true)
    @CsvBindByPosition(position = 5)
    private String incomeExpense;

    @CsvBindByName(column = "金额", required = true)
    @CsvBindByPosition(position = 6)
    private Double amount;

    @CsvBindByName(column = "收/付款方式", required = true)
    @CsvBindByPosition(position = 7)
    private String payerPayee;

    @CsvBindByName(column = "交易状态", required = true)
    @CsvBindByPosition(position = 8)
    private String transactionStatus;

    @CsvBindByName(column = "交易订单号", required = true)
    @CsvBindByPosition(position = 9)
    private String transactionOrderId;

    @CsvBindByName(column = "商家订单号", required = true)
    @CsvBindByPosition(position = 10)
    private String merchantOrderId;

    @CsvBindByName(column = "备注", required = false) // 允许为空
    @CsvBindByPosition(position = 11)
    private String remarks;

    // 无参构造函数
    public AlipayBillDTO() {
    }

    // 全参构造函数（可选）
    public AlipayBillDTO(String transactionTime, String transactionCategory,
                         String transactionCounterparty, String counterpartyAccount,
                         String productDescription, String incomeExpense, Double amount,
                         String payerPayee, String transactionStatus, String transactionOrderId,
                         String merchantOrderId, String remarks) {
        this.transactionTime = transactionTime;
        this.transactionCategory = transactionCategory;
        this.transactionCounterparty = transactionCounterparty;
        this.counterpartyAccount = counterpartyAccount;
        this.productDescription = productDescription;
        this.incomeExpense = incomeExpense;
        this.amount = amount;
        this.payerPayee = payerPayee;
        this.transactionStatus = transactionStatus;
        this.transactionOrderId = transactionOrderId;
        this.merchantOrderId = merchantOrderId;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "AlipayBillDTO{" +
                "transactionTime=" + transactionTime +
                ", transactionCategory='" + transactionCategory + '\'' +
                ", transactionCounterparty='" + transactionCounterparty + '\'' +
                ", counterpartyAccount='" + counterpartyAccount + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", incomeExpense='" + incomeExpense + '\'' +
                ", amount=" + amount +
                ", payerPayee='" + payerPayee + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", transactionOrderId='" + transactionOrderId + '\'' +
                ", merchantOrderId='" + merchantOrderId + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
