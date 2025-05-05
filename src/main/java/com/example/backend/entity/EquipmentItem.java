package com.example.backend.entity;


import com.example.backend.enums.EquipmentItemStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "equipment_item")
public class EquipmentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false, unique = true)
    String serialNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentItemStatus status;
    LocalDate purchaseDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    @OneToMany(mappedBy = "equipmentItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Borrowing> borrowing;
    @OneToMany(mappedBy = "equipmentItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Maintenance> maintenance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
}
