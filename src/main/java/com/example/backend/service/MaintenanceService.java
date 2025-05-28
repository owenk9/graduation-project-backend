package com.example.backend.service;

import com.example.backend.dto.request.MaintenanceRequest;
import com.example.backend.dto.response.MaintenanceByTime;
import com.example.backend.dto.response.MaintenanceResponse;
import com.sun.tools.javac.Main;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceService {
    MaintenanceResponse addMaintenance(MaintenanceRequest maintenancerequest);
    MaintenanceResponse updateMaintenance(int id, MaintenanceRequest maintenancerequest);
    void deleteMaintenance(int id);
    MaintenanceResponse findMaintenanceById(int id);
    Page<MaintenanceResponse> getAllMaintenances(Pageable pageable);
    Page<MaintenanceResponse> findMaintenanceByTechnician(String technician, Pageable pageable);
    long getTotalMaintenances();
    Page<MaintenanceResponse> findByEquipmentItemId(int equipmentItemId, Pageable pageable);
    List<MaintenanceByTime> getMaintenanceByTime(LocalDateTime startDate, LocalDateTime endDate, String groupBy);
    Page<MaintenanceResponse> getMaintenanceReports(Integer year, String month, String quarter, Pageable pageable);
    Double getTotalMaintenanceCost();
    Page<MaintenanceResponse> searchMaintenanceByEquipmentName(String equipmentName, Pageable pageable);
}
