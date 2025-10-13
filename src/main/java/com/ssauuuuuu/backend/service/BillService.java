package com.ssauuuuuu.backend.service;

import com.ssauuuuuu.backend.dto.BillDTO;
import com.ssauuuuuu.backend.model.Bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BillService {

    List<Bill> getAllBills();

    void saveBills(List<Bill> bills);

    List<BillDTO> getBillByMonth(LocalDateTime month, Integer userId);

    List<BillDTO.WeeklySummary> getWeeklySummaryByMonth(LocalDateTime month, Integer userId);

    Map<Integer, List<BillDTO.WeeklySummary>> getYearlySummaryByWeek(Integer year, Integer userId);
}