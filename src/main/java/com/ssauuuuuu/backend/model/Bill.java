package com.ssauuuuuu.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "source_type_id", nullable = false)
    private Integer sourceTypeId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    @Column(name = "order_amount", precision = 10, scale = 2)
    private BigDecimal orderAmount;

    @Column(name = "voucher_amount", precision = 10, scale = 2)
    private BigDecimal voucherAmount;

    @Column(name = "pay_type", nullable = false)
    private Integer payType;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "third_party_order_no", length = 100)
    private String thirdPartyOrderNo;

    @Column(name = "merchant_order_no", length = 100)
    private String merchantOrderNo;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    // Getters and Setters
    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(Integer sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getThirdPartyOrderNo() {
        return thirdPartyOrderNo;
    }

    public void setThirdPartyOrderNo(String thirdPartyOrderNo) {
        this.thirdPartyOrderNo = thirdPartyOrderNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
