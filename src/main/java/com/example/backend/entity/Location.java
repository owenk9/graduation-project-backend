package com.example.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String locationName;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Equipment> equipment;
}
