package com.example.backend.repository;

import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    boolean existsByName(String name);
    Page<Equipment> findByLocationId(int locationId, Pageable pageable);
    Page<Equipment> findByCategoryId(int categoryId, Pageable pageable);
    @Query("select count(e) from Equipment e")
    long getTotalEquipment();
    long countByStatus(String status);
    Page<Equipment> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Equipment> findByStatus(String status, Pageable pageable);
    @Query("SELECT DISTINCT e.status FROM Equipment e")
    List<String> findAllDistinctStatuses();

    @Query(value = "select * from Equipment e where (:locationId is null or e.location_id = :locationId) and (:categoryId is null or e.category_id = :categoryId) "+
            "and (:name is null or e.name ilike concat('%', :name, '%') ) and (:status is null or e.status = :status)",
            nativeQuery = true)
    Page<Equipment> filter(@Param("locationId") Integer locationId,
                           @Param("categoryId") Integer categoryId,
                           @Param("name") String name,
                           @Param("status") String status,
                           Pageable pageable);
}