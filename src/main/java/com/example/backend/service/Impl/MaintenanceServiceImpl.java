package com.example.backend.service.Impl;

import com.example.backend.dto.request.MaintenanceRequest;
import com.example.backend.dto.response.MaintenanceByTime;
import com.example.backend.dto.response.MaintenanceResponse;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.EquipmentItem;
import com.example.backend.entity.Maintenance;
import com.example.backend.enums.MaintenanceStatus;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.MaintenanceMapper;
import com.example.backend.repository.EquipmentItemRepository;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.repository.MaintenanceRepository;
import com.example.backend.service.MaintenanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {
    MaintenanceMapper maintenanceMapper;
    MaintenanceRepository maintenanceRepository;
    EquipmentRepository equipmentRepository;
    EquipmentItemRepository equipmentItemRepository;
    private Equipment findEquipmentById(int id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
    }
    private EquipmentItem findEquipmentItemById(int id) {
        return equipmentItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipment item not found with id: " + id));
    }
    @Override
    public MaintenanceResponse addMaintenance(MaintenanceRequest maintenancerequest) {
        if(equipmentItemRepository.existsBySerialNumber(maintenancerequest.getSerialNumber())){
            throw new DuplicateResourceException("Serial number already exists: " + maintenancerequest.getSerialNumber());
        }
        Maintenance maintenance = maintenanceMapper.toMaintenance(maintenancerequest);
        maintenance.setEquipmentItem(findEquipmentItemById(maintenancerequest.getEquipmentItemId()));
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toMaintenanceResponse(savedMaintenance);
    }

    @Override
    public MaintenanceResponse updateMaintenance(int id, MaintenanceRequest maintenancerequest) {
        Maintenance existingMaintenance = maintenanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + id));
        existingMaintenance.setEquipmentItem(findEquipmentItemById(maintenancerequest.getEquipmentItemId()));
        existingMaintenance.setMaintenanceDate(maintenancerequest.getMaintenanceDate());
        existingMaintenance.setDescription(maintenancerequest.getDescription());
        existingMaintenance.setStatus(MaintenanceStatus.valueOf(maintenancerequest.getStatus()));
        existingMaintenance.setCost(maintenancerequest.getCost());
        existingMaintenance.setTechnician(maintenancerequest.getTechnician());
        Maintenance updatedMaintenance = maintenanceRepository.save(existingMaintenance);
        return maintenanceMapper.toMaintenanceResponse(updatedMaintenance);
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

    @Override
    public Page<MaintenanceResponse> getAllMaintenances(Pageable pageable) {
        Page<Maintenance> maintenances = maintenanceRepository.findAll(pageable);
        return maintenances.map(maintenanceMapper::toMaintenanceResponse);
    }


    @Override
    public Page<MaintenanceResponse> findMaintenanceByTechnician(String technician, Pageable pageable) {
        Page<Maintenance> maintenance = maintenanceRepository.findByTechnicianContainingIgnoreCase(technician, pageable);
        if(maintenance.isEmpty()){
            throw new ResourceNotFoundException("Maintenance not found with technician: " + technician);
        }
        return maintenance.map(maintenanceMapper::toMaintenanceResponse);
    }


    @Override
    public long getTotalMaintenances() {
        return maintenanceRepository.getTotalMaintenance();
    }

    @Override
    public Page<MaintenanceResponse> findByEquipmentItemId(int equipmentItemId, Pageable pageable) {
        Page<Maintenance> history = maintenanceRepository.findByEquipmentItemId(equipmentItemId, pageable);
        return history.map(maintenanceMapper::toMaintenanceResponse);
    }

    public List<MaintenanceByTime> getMaintenanceByTime(LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        List<Object[]> rawResults;
        if ("month".equalsIgnoreCase(groupBy)) {
            rawResults = maintenanceRepository.countMaintenanceByMonth(startDate, endDate);
        } else if ("quarter".equalsIgnoreCase(groupBy)) {
            // Tương tự, dùng FUNCTION('DATE_FORMAT', m.maintenanceDate, '%Y-Q%q')
            rawResults = maintenanceRepository.countMaintenanceByQuarter(startDate, endDate);
        } else {
            rawResults = maintenanceRepository.countMaintenanceByMonth(startDate, endDate);
        }
        return rawResults.stream()
                .map(result -> new MaintenanceByTime(
                        (String) result[0],
                        ((Number) result[1]).longValue(),
                        ((Number) result[2]).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Page<MaintenanceResponse> getMaintenanceReports(Integer year, String month, String quarter, Pageable pageable) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (year != null) {
            if (month != null && !month.isEmpty()) {
                startDate = LocalDateTime.of(year, Integer.parseInt(month), 1, 0, 0);
                endDate = startDate.plusMonths(1).minusSeconds(1);
            } else if (quarter != null && !quarter.isEmpty()) {
                int startMonth = (Integer.parseInt(quarter) - 1) * 3 + 1;
                startDate = LocalDateTime.of(year, startMonth, 1, 0, 0);
                endDate = startDate.plusMonths(3).minusSeconds(1);
            } else {
                startDate = LocalDateTime.of(year, 1, 1, 0, 0);
                endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59);
            }
        }

        if (startDate == null || endDate == null) {
            return maintenanceRepository.findAll(pageable).map(maintenanceMapper::toMaintenanceResponse);
        }

        return maintenanceRepository.findByMaintenanceDateBetween(startDate, endDate, pageable)
                .map(maintenanceMapper::toMaintenanceResponse);
    }

    @Override
    public Double getTotalMaintenanceCost() {
        Double totalCost = maintenanceRepository.sumMaintenanceCost();
        return totalCost != null ? totalCost : 0.0;
    }

    @Override
    public Page<MaintenanceResponse> searchMaintenanceByEquipmentName(String equipmentName, Pageable pageable) {
       Page<Maintenance> maintenances = maintenanceRepository.findByEquipmentNameContainingIgnoreCase(equipmentName, pageable);
       return maintenances.map(maintenanceMapper::toMaintenanceResponse);
    }

}
