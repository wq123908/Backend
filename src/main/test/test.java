import com.ssauuuuuu.backend.model.Bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class test {
//    public static void main(String[] args) {
//        Bill bill = new Bill();
//        bill.setUserId(1);
//
//        // 定义多种可能的日期格式以提高兼容性
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");
//        String dateTimeStr = "2025-6-30 15:55:54";
//
//        try {
//            bill.setTransactionTime(LocalDateTime.parse(dateTimeStr, formatter));
//            System.out.println("解析成功: " + bill.getTransactionTime());
//        } catch (Exception e) {
//            System.err.println("日期解析失败: " + e.getMessage());
//        }
//    }
        public static void main(String[] args) {
            Bill bill = new Bill();
            bill.setAmount(BigDecimal.valueOf(10.0));
            System.out.println(bill.getAmount());
        }
}
