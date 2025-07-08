package com.ssauuuuuu.backend.service.impl;

import com.ssauuuuuu.backend.model.Bill;
import com.ssauuuuuu.backend.repository.BillRepository;
import com.ssauuuuuu.backend.service.BillService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
}