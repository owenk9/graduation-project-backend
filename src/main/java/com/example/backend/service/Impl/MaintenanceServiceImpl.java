package com.example.backend.service.Impl;

import com.example.backend.dto.request.MaintenanceRequest;
import com.example.backend.dto.response.MaintenanceResponse;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.Maintenance;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.MaintenanceMapper;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.repository.MaintenanceRepository;
import com.example.backend.service.MaintenanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {
    MaintenanceMapper maintenanceMapper;
    MaintenanceRepository maintenanceRepository;
    EquipmentRepository equipmentRepository;
    private Equipment findEquipmentById(int id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
    }
    @Override
    public MaintenanceResponse addMaintenance(MaintenanceRequest maintenancerequest) {
        Maintenance maintenance = maintenanceMapper.toMaintenance(maintenancerequest);
        maintenance.setEquipment(findEquipmentById(maintenancerequest.getEquipmentId()));
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toMaintenanceResponse(savedMaintenance);
    }

    @Override
    public MaintenanceResponse updateMaintenance(MaintenanceRequest maintenancerequest) {
        Maintenance maintenance = maintenanceMapper.toMaintenance(maintenancerequest);
        maintenance.setEquipment(findEquipmentById(maintenancerequest.getEquipmentId()));
        maintenance.setMaintenanceDate(maintenancerequest.getMaintenanceDate());
        maintenance.setDescription(maintenancerequest.getDescription());
        maintenance.setCost(maintenancerequest.getCost());
        maintenance.setTechnician(maintenancerequest.getTechnician());
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toMaintenanceResponse(savedMaintenance);
    }

    @Override
    public void deleteMaintenance(int id) {
         maintenanceRepository.deleteById(id);
    }

    @Override
    public MaintenanceResponse findMaintenanceById(int id) {
        Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + id));
        return maintenanceMapper.toMaintenanceResponse(maintenance);
    }
}
