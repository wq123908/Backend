package com.ssauuuuuu.backend.convert;

import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import com.ssauuuuuu.backend.dto.BillDTO;
import com.ssauuuuuu.backend.model.Bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUploadAlipayConvertBill {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");


    public static Bill convertToBillDTO(AlipayBillDTO alipayBill) {
        Bill bill = new Bill();
        // 设置用户ID（需要从上下文获取或作为参数传入）
        bill.setUserId(1); // 示例值，实际应从安全上下文获取 TODO
        // 设置来源类型ID（支付宝对应固定值）
        bill.setSourceTypeId(1); // 假设1代表支付宝
        // 设置分类ID（需要映射逻辑）
        bill.setCategoryId(mapCategory(alipayBill.getTransactionCategory()));
        // 设置金额
        bill.setAmount(BigDecimal.valueOf(alipayBill.getAmount()));
        // 设置交易时间
        bill.setTransactionTime(LocalDateTime.parse(alipayBill.getTransactionTime(),formatter));
        // 设置收支类型
        bill.setPayType(convertPayType(alipayBill.getIncomeExpense()));
        // 设置备注
        bill.setRemark(alipayBill.getRemarks());
        // 商户订单号
        bill.setMerchantOrderNo(alipayBill.getMerchantOrderId());
        // 设置订单号
        bill.setThirdPartyOrderNo(alipayBill.getTransactionOrderId());
        return bill;
    }

    public static Integer mapCategory(String alipayCategory) {
        // 实现支付宝分类到系统分类的映射逻辑
        // 这里需要根据实际业务需求实现
        return 1; // 示例值
    }

    /**
     * 转换收支类型
     * @param incomeExpense 收支标识字符串
     * @return 1-收入, 2-支出, 3-不记收支
     */
    public static Integer convertPayType(String incomeExpense) {
        if (incomeExpense == null) {
            return 3; // 默认不记收支
        }

        switch (incomeExpense) {
            case "收入":
                return 1;
            case "支出":
                return 2;
            case "不记收支":
                return 3;
            default:
                return 3; // 默认不记收支
        }
    }

}
