package com.example.backend.repository;

import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    Page<Equipment> findEquipmentByLocationId(int locationId, Pageable pageable);
    Page<Equipment> findEquipmentByCategoryId(int categoryId, Pageable pageable);
}
