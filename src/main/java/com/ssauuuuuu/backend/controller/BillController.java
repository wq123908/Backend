package com.ssauuuuuu.backend.controller;

import com.ssauuuuuu.backend.common.Response;
import com.ssauuuuuu.backend.common.ResponseUtil;
import com.ssauuuuuu.backend.dto.BillDTO;
import com.ssauuuuuu.backend.service.BillService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Operation(
        summary = "获取所有账单",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "获取成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            )
        }
    )
    public ResponseEntity<Response<String>> getAllBills() {
        return ResponseUtil.success("获取成功", "Bill list");
    }


    @GetMapping("/month")
    @Operation(summary = "按月份获取账单",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "参数错误")
        })
    public ResponseEntity<Response<List<BillDTO>>> getBillsByMonth(@RequestParam("month") String month, @RequestParam("userId") Integer userId) {
        try {
            LocalDateTime monthDate = LocalDateTime.parse(String.valueOf(parseMonthString(month)));
            List<BillDTO> bills = billService.getBillByMonth(monthDate, userId);
            return ResponseUtil.success("获取成功", bills);
        } catch (Exception e) {
            return ResponseUtil.failed("参数错误");
        }
    }

    @GetMapping("/week-summary")
    @Operation(summary = "获取月度周汇总",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "参数错误")
        })
    public ResponseEntity<Response<List<BillDTO.WeeklySummary>>> getWeeklySummaryByMonth(@RequestParam("month") String month, @RequestParam("userId") Integer userId) {
        try {
            LocalDateTime monthDate = parseMonthString(month);
            List<BillDTO.WeeklySummary> summaries = billService.getWeeklySummaryByMonth(monthDate, userId);
            return ResponseUtil.success("获取成功", summaries);
        } catch (Exception e) {
            return ResponseUtil.failed("参数错误");
        }
    }

    @GetMapping("/year-summary")
    @Operation(summary = "获取年度汇总",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "参数错误")
        })
    public ResponseEntity<Response<Map<Integer, List<BillDTO.WeeklySummary>>>> getYearlySummary(@RequestParam("year") Integer year, @RequestParam("userId") Integer userId) {
        try {
            if (year == null) {
                year = LocalDateTime.now().getYear(); // 默认使用当前年份
            }
            Map<Integer, List<BillDTO.WeeklySummary>> summaries = billService.getYearlySummaryByWeek(year, userId);
            return ResponseUtil.success("获取成功", summaries);
        } catch (Exception e) {
            return ResponseUtil.failed("参数错误");
        }
    }
}