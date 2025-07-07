package com.example.bill.controller;

import org.springframework.web.bind.annotation.*;

@RestController  // 处理账单相关API请求
@RequestMapping("/api/bills")
public class BillController {
    @GetMapping
    public String getAllBills() {
        return "Bill list";
    }
}