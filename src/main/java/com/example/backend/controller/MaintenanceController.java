package com.example.backend.controller;

import com.example.backend.dto.request.MaintenanceRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.dto.response.MaintenanceResponse;
import com.example.backend.entity.Maintenance;
import com.example.backend.exception.InvalidRequestException;
import com.example.backend.service.MaintenanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maintenance")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MaintenanceController {
    MaintenanceService maintenanceService;
    @PostMapping("/add")
    public ResponseEntity<MaintenanceResponse> addMaintenance(@RequestBody MaintenanceRequest maintenanceRequest) {
        MaintenanceResponse maintenance = maintenanceService.addMaintenance(maintenanceRequest);
        return ResponseEntity.status(201).body(maintenance);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<MaintenanceResponse> updateMaintenance(@PathVariable int id, @RequestBody MaintenanceRequest maintenanceRequest) {
        MaintenanceResponse maintenance = maintenanceService.updateMaintenance(id, maintenanceRequest);
        return ResponseEntity.ok(maintenance);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMaintenance(@PathVariable int id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.status(200).body("Successfully deleted maintenance with id: " + id);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MaintenanceResponse> getMaintenanceById(@PathVariable int id) {
        MaintenanceResponse maintenance = maintenanceService.findMaintenanceById(id);
        return ResponseEntity.ok(maintenance);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<MaintenanceResponse>> getMaintenance(@RequestParam(required = false) Integer equipmentId,
                                                              @RequestParam(required = false) String technician,
                                                              @RequestParam(required = false) String name,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MaintenanceResponse> maintenancePage;
        if (equipmentId != null && (technician != null || name != null)) {
            throw new InvalidRequestException("Cannot combine equipmentId with technician or name");
        } else if (technician != null && name != null) {
            throw new InvalidRequestException("Cannot combine technician with name");
        } else if (name != null) {
            maintenancePage = maintenanceService.findMaintenanceByEquipmentName(name, pageable);
        } else if (equipmentId != null) {
            maintenancePage = maintenanceService.findMaintenanceByEquipmentId(equipmentId, pageable);
        } else if (technician != null) {
            maintenancePage = maintenanceService.findMaintenanceByTechnician(technician, pageable);
        } else {
            maintenancePage = maintenanceService.getAllMaintenances(pageable);
        }

        return ResponseEntity.ok(maintenancePage);    }
}
