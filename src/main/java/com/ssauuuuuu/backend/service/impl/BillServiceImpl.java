package com.ssauuuuuu.backend.service.impl;

import com.ssauuuuuu.backend.dto.BillDTO;
import com.ssauuuuuu.backend.model.Bill;
import com.ssauuuuuu.backend.repository.BillRepository;
import com.ssauuuuuu.backend.service.BillService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public void saveBills(List<Bill> bills) { billRepository.saveAll(bills);}

   @Override
    public List<BillDTO> getBillByMonth(LocalDateTime month, Integer userId) {
        if (month == null) {
            throw new IllegalArgumentException("month cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        // 获取月份的开始和结束时间
        LocalDateTime startOfMonth = month.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = month.toLocalDate().withDayOfMonth(month.toLocalDate().lengthOfMonth()).atTime(LocalTime.MAX);

        // 查询数据库获取指定月份和用户的所有账单
        List<Bill> bills = billRepository.findByUserIdAndTransactionTimeBetween(userId, startOfMonth, endOfMonth);

        // 转换为BillDTO列表
        return bills.stream()
                .map(bill -> new BillDTO(
                        bill.getBillId(),              // billId
                        bill.getUserId(),              // userId
                        bill.getSourceTypeId(),        // sourceTypeId
                        bill.getCategoryId(),          // categoryId
                        bill.getAmount(),              // amount
                        bill.getTransactionTime(),     // transactionTime
                        bill.getOrderAmount(),         // orderAmount
                        bill.getVoucherAmount(),       // voucherAmount
                        bill.getPayType(),             // payType
                        bill.getRemark(),              // remark
                        bill.getThirdPartyOrderNo(),   // thirdPartyOrderNo
                        bill.getMerchantOrderNo(),     // merchantOrderNo
                        bill.getCreateTime()           // createTime
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public List<BillDTO.WeeklySummary> getWeeklySummaryByMonth(LocalDateTime month, Integer userId) {
        if (month == null) {
            throw new IllegalArgumentException("month cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        // 获取月份的开始和结束时间
        LocalDateTime startOfMonth = month.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = month.toLocalDate().withDayOfMonth(month.toLocalDate().lengthOfMonth()).atTime(LocalTime.MAX);

        // 查询数据库获取指定月份和用户的所有账单
        List<Bill> bills = billRepository.findByUserIdAndTransactionTimeBetween(userId, startOfMonth, endOfMonth);

        // 转换为BillDTO列表
        List<BillDTO> billDTOs = bills.stream()
                .map(bill -> new BillDTO(
                        bill.getBillId(),              // billId
                        bill.getUserId(),              // userId
                        bill.getSourceTypeId(),        // sourceTypeId
                        bill.getCategoryId(),          // categoryId
                        bill.getAmount(),              // amount
                        bill.getTransactionTime(),     // transactionTime
                        bill.getOrderAmount(),         // orderAmount
                        bill.getVoucherAmount(),       // voucherAmount
                        bill.getPayType(),             // payType
                        bill.getRemark(),              // remark
                        bill.getThirdPartyOrderNo(),   // thirdPartyOrderNo
                        bill.getMerchantOrderNo(),     // merchantOrderNo
                        bill.getCreateTime()           // createTime
                ))
                .toList();

        // 按周分组并统计金额，然后转换为所需的格式
        Map<String, BigDecimal> weeklySummaryMap = billDTOs.stream()
                .collect(Collectors.groupingBy(
                        this::getWeekKey,
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, BillDTO::getAmount, BigDecimal::add)
                ));

        // 转换为所需的列表格式
        return weeklySummaryMap.entrySet().stream()
                .map(entry -> new BillDTO.WeeklySummary(
                        entry.getKey().replaceAll("周 \\(.*\\)", "周"), // 只保留"第N周"部分
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, List<BillDTO.WeeklySummary>> getYearlySummaryByWeek(Integer year, Integer userId) {
        if (year == null) {
            year = LocalDateTime.now().getYear();
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        // 获取年份的开始和结束时间
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        // 查询数据库获取指定年份和用户的所有账单
        List<Bill> bills = billRepository.findByUserIdAndTransactionTimeBetween(userId, startOfYear, endOfYear);

        // 转换为BillDTO列表
        List<BillDTO> billDTOs = bills.stream()
                .filter(bill -> bill.getPayType() != null && bill.getPayType() == 2) // 只保留payType为2的数据
                .map(bill -> new BillDTO(
                        bill.getBillId(),              // billId
                        bill.getUserId(),              // userId
                        bill.getSourceTypeId(),        // sourceTypeId
                        bill.getCategoryId(),          // categoryId
                        bill.getAmount(),              // amount
                        bill.getTransactionTime(),     // transactionTime
                        bill.getOrderAmount(),         // orderAmount
                        bill.getVoucherAmount(),       // voucherAmount
                        bill.getPayType(),             // payType
                        bill.getRemark(),              // remark
                        bill.getThirdPartyOrderNo(),   // thirdPartyOrderNo
                        bill.getMerchantOrderNo(),     // merchantOrderNo
                        bill.getCreateTime()           // createTime
                ))
                .toList();


        // 按实际周分组统计金额
        Map<String, BigDecimal> weeklySummaryMap = billDTOs.stream()
                .collect(Collectors.groupingBy(
                        this::getActualWeekKey,
                        Collectors.reducing(BigDecimal.ZERO, BillDTO::getAmount, BigDecimal::add)
                ));

        // 构建按月份分组的返回结果
        Map<Integer, List<BillDTO.WeeklySummary>> result = new LinkedHashMap<>();

// 按月份组织数据
        weeklySummaryMap.forEach((weekKey, amount) -> {
            // 从 weekKey 中提取月份信息
            // weekKey 格式: "第X周 (YYYY-MM-DD至YYYY-MM-DD)"
            try {
                // 提取日期范围部分
                int startIndex = weekKey.indexOf('(');
                int endIndex = weekKey.indexOf(')');
                if (startIndex > 0 && endIndex > startIndex) {
                    String dateRange = weekKey.substring(startIndex + 1, endIndex);
                    String[] dates = dateRange.split("至");
                    if (dates.length >= 1) {
                        LocalDate startDate = LocalDate.parse(dates[0]);
                        int month = startDate.getMonthValue();

                        // 确保月份列表存在
                        result.computeIfAbsent(month, k -> new ArrayList<>());

                        // 添加数据
                        result.get(month).add(new BillDTO.WeeklySummary(weekKey, amount));
                    }
                }
            } catch (Exception e) {
                // 忽略解析错误
            }
        });

// 按时间顺序排序每个月的数据，并确保所有月份都有数据
        for (int month = 1; month <= 12; month++) {
            if (result.containsKey(month)) {
                // 按周起始日期排序
                result.get(month).sort((a, b) -> {
                    try {
                        // 从 week 字段提取起始日期进行排序
                        String aKey = a.getWeek();
                        String bKey = b.getWeek();

                        String aDateStr = aKey.substring(aKey.indexOf('(') + 1, aKey.indexOf('至'));
                        String bDateStr = bKey.substring(bKey.indexOf('(') + 1, bKey.indexOf('至'));

                        return LocalDate.parse(aDateStr).compareTo(LocalDate.parse(bDateStr));
                    } catch (Exception e) {
                        return 0;
                    }
                });
            } else {
                result.put(month, new ArrayList<>());
            }
        }

        return result;
    }

    // 获取月份-周标识key的方法
    private String getMonthWeekKey(BillDTO billDTO) {
        LocalDateTime transactionTime = billDTO.getTransactionTime();
        LocalDate date = transactionTime.toLocalDate();

        // 获取月份
        int month = date.getMonthValue();

        // 获取月份中的周数
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate firstMonday = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() - 1);
        int weekNumber = (int) ((date.toEpochDay() - firstMonday.toEpochDay()) / 7) + 1;

        return month + "-第" + weekNumber + "周";
    }

    // 获取年份中的周标识key的方法
    private String getYearWeekKey(BillDTO billDTO) {
        LocalDateTime transactionTime = billDTO.getTransactionTime();
        LocalDate date = transactionTime.toLocalDate();

        // 获取年份中的周数
        int weekOfYear = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return "第" + weekOfYear + "周";
    }


    private String getWeekKey(BillDTO billDTO) {
        LocalDateTime transactionTime = billDTO.getTransactionTime();
        LocalDate date = transactionTime.toLocalDate();

        // 获取月份的第一天和最后一天
        LocalDate monthStart = date.withDayOfMonth(1);
        LocalDate monthEnd = date.withDayOfMonth(date.lengthOfMonth());

        // 计算本周的周一和周日
        LocalDate weekStart = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        // 确保周范围不超出月份范围
        LocalDate actualStart = weekStart.isBefore(monthStart) ? monthStart : weekStart;
        LocalDate actualEnd = weekEnd.isAfter(monthEnd) ? monthEnd : weekEnd;

        // 计算这是该月的第几周
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate firstMonday = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() - 1);
        int weekNumber = (int) ((actualStart.toEpochDay() - firstMonday.toEpochDay()) / 7) + 1;

        return "第" + weekNumber + "周 (" + actualStart.toString() + "至" + actualEnd.toString() + ")";
    }

    // 获取实际周标识key的方法（包含起止日期）
    private String getActualWeekKey(BillDTO billDTO) {
        LocalDateTime transactionTime = billDTO.getTransactionTime();
        LocalDate date = transactionTime.toLocalDate();

        // 获取月份的第一天和最后一天
        LocalDate monthStart = date.withDayOfMonth(1);
        LocalDate monthEnd = date.withDayOfMonth(date.lengthOfMonth());

        // 计算本周的周一和周日
        LocalDate weekStart = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        // 确保周范围不超出月份范围
        LocalDate actualStart = weekStart.isBefore(monthStart) ? monthStart : weekStart;
        LocalDate actualEnd = weekEnd.isAfter(monthEnd) ? monthEnd : weekEnd;

        // 计算这是该月的第几周（从1开始）
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate firstMonday = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() - 1);
        int weekNumber = (int) ((actualStart.toEpochDay() - firstMonday.toEpochDay()) / 7) + 1;

        return "第" + weekNumber + "周 (" + actualStart.toString() + "至" + actualEnd.toString() + ")";
    }

}