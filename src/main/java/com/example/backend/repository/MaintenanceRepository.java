package com.example.backend.repository;

import com.example.backend.entity.Maintenance;
import com.sun.tools.javac.Main;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    Page<Maintenance> findByTechnicianContainingIgnoreCase(String technician, Pageable pageable);
    @Query("select count(m) from Maintenance m")
    long getTotalMaintenance();
    Page<Maintenance> findByEquipmentItemId(int equipmentItemId, Pageable pageable);
    Page<Maintenance> findByMaintenanceDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    @Query(value = "SELECT TO_CHAR(m.maintenance_date, 'YYYY-MM') as time_period, COUNT(m.id) as count, COALESCE(SUM(m.cost), 0) as total_cost " +
            "FROM maintenance m " +
            "WHERE m.maintenance_date >= ?1 AND m.maintenance_date <= ?2 " +
            "GROUP BY TO_CHAR(m.maintenance_date, 'YYYY-MM') " +
            "ORDER BY time_period", nativeQuery = true)
    List<Object[]> countMaintenanceByMonth(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT TO_CHAR(m.maintenance_date, 'YYYY-\"Q\"Q') as time_period, COUNT(m.id) as count, COALESCE(SUM(m.cost), 0) as total_cost " +
            "FROM maintenance m " +
            "WHERE m.maintenance_date >= ?1 AND m.maintenance_date <= ?2 " +
            "GROUP BY TO_CHAR(m.maintenance_date, 'YYYY-\"Q\"Q') " +
            "ORDER BY time_period", nativeQuery = true)
    List<Object[]> countMaintenanceByQuarter(LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT COALESCE(SUM(m.cost), 0) FROM Maintenance m")
    Double sumMaintenanceCost();
}

