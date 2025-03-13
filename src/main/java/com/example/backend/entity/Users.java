package com.example.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String fullName;
    String email;
    String department;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Borrowing> borrowing;
}
