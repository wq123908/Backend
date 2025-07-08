// 数据访问层接口继承Spring Data JPA
package com.ssauuuuuu.backend.repository;

import com.ssauuuuuu.backend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT SUM(b.amount) FROM Bill b WHERE b.transactionType = 'EXPENDITURE' AND b.transactionTime BETWEEN :start AND :end")
    BigDecimal sumExpenditureByPeriod(@Param("start") LocalDate startDate, @Param("end") LocalDate endDate);

    @Query("SELECT AVG(b.amount) FROM Bill b WHERE b.transactionType = 'INCOME' AND b.category = :category GROUP BY FUNCTION('DATE_TRUNC', :period, b.transactionTime)")
    BigDecimal averageIncomeByCategory(@Param("category") String category, @Param("period") String period);
}