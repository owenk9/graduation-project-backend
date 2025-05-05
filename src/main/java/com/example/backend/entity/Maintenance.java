package com.example.backend.entity;

import com.example.backend.enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "maintenance")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_item_id", nullable = false)
    private EquipmentItem equipmentItem;
    @Column(nullable = false)
    private LocalDateTime maintenanceDate;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceStatus status;
    private double cost;
    private String technician;
}
