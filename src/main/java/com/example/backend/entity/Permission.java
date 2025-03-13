package com.example.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String permissionName;
    String description;
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePermission> rolePermission;
}
