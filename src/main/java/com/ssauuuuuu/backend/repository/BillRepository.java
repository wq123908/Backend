// 数据访问层接口继承Spring Data JPA
package com.ssauuuuuu.backend.repository;

import com.ssauuuuuu.backend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByUserIdAndTransactionTimeBetween(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

}