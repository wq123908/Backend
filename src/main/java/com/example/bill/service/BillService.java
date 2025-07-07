package com.example.bill.service;

import com.example.bill.model.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BillService {

    List<Bill> getAllBills();
}