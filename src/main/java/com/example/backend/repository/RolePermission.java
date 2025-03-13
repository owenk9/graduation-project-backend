package com.example.backend.repository;

import com.example.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermission extends JpaRepository<com.example.backend.entity.RolePermission, Integer> {
}
