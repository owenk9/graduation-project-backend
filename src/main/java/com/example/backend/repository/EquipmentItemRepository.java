package com.example.backend.repository;

import com.example.backend.entity.EquipmentItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, Integer> {
    Page<EquipmentItem> findByLocationId(int locationId, Pageable pageable);
    boolean existsBySerialNumber(String serialNumber);
    @Query("select count(e) from EquipmentItem e")
    long getTotalEquipmentItem();
    List<EquipmentItem> findAllEquipmentItemByEquipmentId(int equipmentId);
}
