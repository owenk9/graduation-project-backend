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
    public ResponseEntity<Page<MaintenanceResponse>> getMaintenance(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "maintenanceDate");
       Pageable pageable = PageRequest.of(page, size, sort);
       Page<MaintenanceResponse> maintenanceResponses = maintenanceService.getAllMaintenances(pageable);
       return ResponseEntity.ok(maintenanceResponses);
    }
    @GetMapping("/history")
    public ResponseEntity<Page<MaintenanceResponse>> history(@RequestParam("equipmentItemId") Integer equipmentItemId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size)
    {
        Sort sort = Sort.by(Sort.Direction.DESC, "maintenanceDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MaintenanceResponse> history = maintenanceService.findByEquipmentItemId(equipmentItemId, pageable);
        return ResponseEntity.ok(history);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<MaintenanceResponse>> searchMaintenanceByEquipmentName(@RequestParam(required = false) String name,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "maintenanceDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MaintenanceResponse> maintenanceResponses = maintenanceService.searchMaintenanceByEquipmentName(name, pageable);
        return ResponseEntity.ok(maintenanceResponses);
    }


}
