package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    LocalDateTime purchaseDate;
    String description;
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Borrowing> borrowing;
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Maintenance> maintenance;

    public void setCategory(Category category) {
        this.category = category;
    }
}
