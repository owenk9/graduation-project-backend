package com.example.backend.repository;

import com.example.backend.entity.Maintenance;
import com.sun.tools.javac.Main;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    Page<Maintenance> findMaintenanceByEquipmentId(int equipmentId, Pageable pageable);
    Page<Maintenance> findByTechnicianContainingIgnoreCase(String technician, Pageable pageable);
    @Query("select m from Maintenance m where lower(m.equipment.name) like lower(concat('%', :name, '%') ) ")
    Page<Maintenance> findByEquipmentNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("select count(m) from Maintenance m")
    long getTotalMaintenance();
    @Query("select m.equipment.name from Maintenance m  where  m.equipment.id = :equipmentId")
    Page<Maintenance> findEquipmentNameByEquipmentId(int equipmentId, Pageable pageable);
}
