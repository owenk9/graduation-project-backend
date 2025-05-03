package com.example.backend.repository;

import com.example.backend.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    boolean existsByName(String name);

    @Query("SELECT DISTINCT e FROM Equipment e LEFT JOIN e.equipmentItem ei WHERE e.category.id = :categoryId ORDER BY e.id")
    Page<Equipment> findEquipmentByCategoryId(@Param("categoryId") int categoryId, Pageable pageable);

    @Query("SELECT DISTINCT e FROM Equipment e LEFT JOIN e.equipmentItem ei WHERE ei.location.id = :locationId ORDER BY e.id")
    Page<Equipment> findEquipmentByLocationId(@Param("locationId") int locationId, Pageable pageable);

    @Query("SELECT DISTINCT e FROM Equipment e LEFT JOIN e.equipmentItem ei WHERE e.category.id = :categoryId AND ei.location.id = :locationId ORDER BY e.id")
    Page<Equipment> findEquipmentByCategoryIdAndLocationId(@Param("categoryId") int categoryId, @Param("locationId") int locationId, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Equipment e")
    long getTotalEquipment();

    Page<Equipment> findByNameContainingIgnoreCase(String name, Pageable pageable);
}