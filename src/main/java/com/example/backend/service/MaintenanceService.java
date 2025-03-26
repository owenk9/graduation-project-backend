package com.example.backend.service;

import com.example.backend.dto.request.MaintenanceRequest;
import com.example.backend.dto.response.MaintenanceResponse;
import com.example.backend.entity.Maintenance;
import com.example.backend.repository.MaintenanceRepository;

public interface MaintenanceService {
    MaintenanceResponse addMaintenance(MaintenanceRequest maintenancerequest);
    MaintenanceResponse updateMaintenance(MaintenanceRequest maintenancerequest);
    void deleteMaintenance(int id);
    MaintenanceResponse findMaintenanceById(int id);
}
