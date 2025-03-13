package com.example.backend.controller;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.exception.InvalidRequestException;
import com.example.backend.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EquipmentController {
    EquipmentService equipmentService;
    @PostMapping("/add")
    public ResponseEntity<EquipmentResponse> addEquipment(@Valid @RequestBody EquipmentRequest equipmentRequest){
        EquipmentResponse addEquipment = equipmentService.addEquipment(equipmentRequest);
        return ResponseEntity.status(201).body(addEquipment);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<EquipmentResponse>> getEquipment(@RequestParam(required = false) Integer locationId,
                                                                @RequestParam(required = false) Integer categoryId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<EquipmentResponse> equipmentPage;
        if (locationId != null && categoryId != null) {
            throw new InvalidRequestException("Cannot provide both locationId and categoryId");
        } else if (locationId != null) {
            equipmentPage = equipmentService.findEquipmentByLocationId(locationId, pageable);
        } else if (categoryId != null) {
            equipmentPage = equipmentService.findEquipmentByCategoryId(categoryId, pageable);
        } else {
            equipmentPage = equipmentService.getAllEquipment(pageable);
        }
        return ResponseEntity.ok(equipmentPage);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable int id){
        EquipmentResponse getEquipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(getEquipment);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<EquipmentResponse> updateEquipment(@PathVariable int id,
                                                             @Valid @RequestBody EquipmentRequest equipmentRequest){
        EquipmentResponse updateEquipment = equipmentService.updateEquipment(id, equipmentRequest);
        return ResponseEntity.ok(updateEquipment);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable int id){
        equipmentService.deleteEquipmentById(id);
        return ResponseEntity.noContent().build();
    }
}
