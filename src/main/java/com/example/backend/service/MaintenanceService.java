package com.example.backend.service;

import com.example.backend.dto.request.MaintenanceRequest;
import com.example.backend.dto.response.MaintenanceResponse;
import com.example.backend.entity.Maintenance;
import com.example.backend.repository.MaintenanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaintenanceService {
    MaintenanceResponse addMaintenance(MaintenanceRequest maintenancerequest);
    MaintenanceResponse updateMaintenance(int id, MaintenanceRequest maintenancerequest);
    void deleteMaintenance(int id);
    MaintenanceResponse findMaintenanceById(int id);
    Page<MaintenanceResponse> getAllMaintenances(Pageable pageable);
    Page<MaintenanceResponse>  findMaintenanceByEquipmentId(int equipmentId, Pageable pageable);
    Page<MaintenanceResponse> findMaintenanceByTechnician(String technician, Pageable pageable);
    long getTotalMaintenances();
}
