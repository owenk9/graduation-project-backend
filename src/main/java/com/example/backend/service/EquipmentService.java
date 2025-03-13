package com.example.backend.service;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipmentService {
    EquipmentResponse addEquipment(EquipmentRequest equipmentRequest);
    EquipmentResponse updateEquipment(int id, EquipmentRequest equipmentRequest);
    EquipmentResponse getEquipmentById(int id);
    Page<EquipmentResponse> getAllEquipment(Pageable pageable);
    void deleteEquipmentById(int id);
    Page<EquipmentResponse> findEquipmentByLocationId(int locationId, Pageable pageable);
    Page<EquipmentResponse> findEquipmentByCategoryId(int categoryId, Pageable pageable);
}
