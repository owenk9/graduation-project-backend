package com.example.backend.entity;


import com.example.backend.enums.BorrowingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "borrowing")
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_item_id", nullable = false)
    private EquipmentItem equipmentItem;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;
    @Column(nullable = false)
    private LocalDateTime borrowDate;
    @Column(nullable = false)
    private LocalDateTime returnDate;
    private String note;
    private String adminNote;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowingStatus status;
}
