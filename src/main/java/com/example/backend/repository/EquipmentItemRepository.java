package com.example.backend.repository;

import com.example.backend.dto.response.StatusDistribution;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.EquipmentItem;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, Integer> {
    Page<EquipmentItem> findByLocationId(int locationId, Pageable pageable);
    boolean existsBySerialNumber(String serialNumber);
    @Query("select count(e) from EquipmentItem e")
    long getTotalEquipmentItem();
    @Query("SELECT e FROM EquipmentItem e WHERE e.equipment.id = :equipmentId ORDER BY e.id")
    List<EquipmentItem> findAllEquipmentItemByEquipmentId(int equipmentId);
    @Query("select e from EquipmentItem e where e.equipment.id = :equipmentId and e.location.id = :locationId order by e.id")
    List<EquipmentItem> findEquipmentItemByEquipmentIdAndLocationId(int equipmentId, int locationId);
    @Query("select e from EquipmentItem e where e.location.id = :locationId order by e.id")
    List<EquipmentItem> findEquipmentItemByLocationId(int locationId);
    @Query("SELECT ei.status, COUNT(ei) " +
            "FROM EquipmentItem ei GROUP BY ei.status")
    List<Object[]> countByStatusRaw();


    Optional<EquipmentItem> findBySerialNumber(@NotBlank(message = "Serial number not null") String serialNumber);
}
