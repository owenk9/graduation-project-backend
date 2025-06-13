package com.example.backend.entity;

import com.example.backend.enums.BrokenStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Broken {
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
    private LocalDateTime brokenDate;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BrokenStatus status = BrokenStatus.PENDING;
}
