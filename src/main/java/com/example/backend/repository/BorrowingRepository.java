package com.example.backend.repository;

import com.example.backend.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {
    @Query("select count(b) from Borrowing b")
    long getTotalBorrowings();
}
