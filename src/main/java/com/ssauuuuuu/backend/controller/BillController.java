package com.ssauuuuuu.backend.controller;

import com.ssauuuuuu.backend.dto.BillDTO;
import com.ssauuuuuu.backend.service.BillService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.ssauuuuuu.backend.utils.utils.parseMonthString;

@RestController  // 处理账单相关API请求
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public String getAllBills() {
        return "Bill list";
    }

    @GetMapping("/month")
    public List<BillDTO> getBillsByMonth(@RequestParam("month") String month, @RequestParam("userId") Integer userId) {
        LocalDateTime monthDate = LocalDateTime.parse(String.valueOf(parseMonthString(month)));
        return billService.getBillByMonth(monthDate, userId);
    }

    @GetMapping("/week-summary")
    public List<BillDTO.WeeklySummary> getWeeklySummaryByMonth(@RequestParam("month") String month, @RequestParam("userId") Integer userId) {
        LocalDateTime monthDate = parseMonthString(month);
        return billService.getWeeklySummaryByMonth(monthDate, userId);
    }

    @GetMapping("/year-summary")
    public Map<Integer, List<BillDTO.WeeklySummary>> getYearlySummary(@RequestParam("year") Integer year, @RequestParam("userId") Integer userId) {
        if (year == null) {
            year = LocalDateTime.now().getYear(); // 默认使用当前年份
        }
        return billService.getYearlySummaryByWeek(year, userId);
    }
}