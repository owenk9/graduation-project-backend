package com.example.backend.repository;

import com.example.backend.entity.Maintenance;
import com.sun.tools.javac.Main;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    Page<Maintenance> findMaintenanceByEquipmentId(int equipmentId, Pageable pageable);
    Page<Maintenance> findMaintenanceByTechnician(String technician, Pageable pageable);
    @Query("select count(m) from Maintenance m")
    long getTotalMaintenance();
}
