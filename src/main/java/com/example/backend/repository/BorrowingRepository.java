package com.example.backend.repository;

import com.example.backend.entity.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {
    Page<Borrowing> findBorrowingByUsersId(int usersId, Pageable pageable);
    Page<Borrowing> findBorrowingByEquipmentId(int equipmentId, Pageable pageable);
    @Query("select count(b) from Borrowing b")
    long getTotalBorrowings();
}
