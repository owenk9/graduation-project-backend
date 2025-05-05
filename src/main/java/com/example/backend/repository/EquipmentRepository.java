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

    @Query("select distinct e from Equipment e left join e.equipmentItem ei where e.category.id = :categoryId order by e.id")
    Page<Equipment> findEquipmentByCategoryId(@Param("categoryId") int categoryId, Pageable pageable);

    @Query("select distinct e from Equipment e left join e.equipmentItem ei where ei.location.id = :locationId order by e.id")
    Page<Equipment> findEquipmentByLocationId(@Param("locationId") int locationId, Pageable pageable);

    @Query("select distinct e from Equipment e left join e.equipmentItem ei where e.category.id = :categoryId and ei.location.id = :locationId order by e.id")
    Page<Equipment> findEquipmentByCategoryIdAndLocationId(@Param("categoryId") int categoryId, @Param("locationId") int locationId, Pageable pageable);

    @Query("select count(e) from Equipment e")
    long getTotalEquipment();

    Page<Equipment> findByNameContainingIgnoreCase(String name, Pageable pageable);
}