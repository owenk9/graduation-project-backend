package com.example.backend.service;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowingService {
    BorrowingResponse addBorrowing(BorrowingRequest borrowingRequest);
    BorrowingResponse updateBorrowing(int id, BorrowingRequest borrowingRequest);
    void deleteBorrowing(int id);
    BorrowingResponse getBorrowingById(int id);
    Page<BorrowingResponse> getAllBorrowings(Pageable pageable);
    long getTotalBorrowings();

}
