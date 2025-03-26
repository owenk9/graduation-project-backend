package com.example.backend.repository;

import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    boolean existsByName(String name);
    Page<Equipment> findByLocationId(int locationId, Pageable pageable);
    Page<Equipment> findByCategoryId(int categoryId, Pageable pageable);
    @Query("select count(e) from Equipment e")
    long getTotalEquipment();
    long countByStatus(String status);
}
