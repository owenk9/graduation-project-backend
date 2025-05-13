package com.example.backend.repository;

import com.example.backend.entity.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {
    Page<Borrowing> findBorrowingByUsersId(int usersId, Pageable pageable);
    Page<Borrowing> findBorrowingByEquipmentItemId(int equipmentItemId, Pageable pageable);
    @Query("select count(b) from Borrowing b")
    long getTotalBorrowings();
    @Query(value = "SELECT b FROM Borrowing b JOIN FETCH b.equipmentItem ei JOIN FETCH ei.equipment e JOIN FETCH b.users u WHERE b.status = 'PENDING'",
            countQuery = "SELECT COUNT(b) FROM Borrowing b WHERE b.status = 'PENDING'")
    Page<Borrowing> findPendingWithAssociations(Pageable pageable);
    @Query("select b from Borrowing b left join b.equipmentItem ei left join ei.equipment e where lower(e.name) like lower(concat('%', :name, '%')) or e.name is null")
    Page<Borrowing> findByEquipmentNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}
