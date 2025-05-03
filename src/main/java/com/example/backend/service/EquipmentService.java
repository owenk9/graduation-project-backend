package com.example.backend.service;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface EquipmentService {
    EquipmentResponse addEquipment(EquipmentRequest equipmentRequest, String imageUrl);
    EquipmentResponse updateEquipment(int id, EquipmentRequest equipmentRequest, String imageUrl);
    EquipmentResponse getEquipmentById(int id);
    Page<EquipmentResponse> getAllEquipment(Pageable pageable);
    void deleteEquipmentById(int id);
//    Page<EquipmentResponse> findEquipmentByLocationId(int locationId, Pageable pageable);
    Page<EquipmentResponse> findEquipmentByCategoryId(int categoryId, Pageable pageable);
    long getTotalEquipment();
//    Map<String, Long> countByStatus();
    Page<EquipmentResponse> findEquipmentByName(String name, Pageable pageable);
//    Page<EquipmentResponse> findEquipmentByStatus(String status, Pageable pageable);
//    List<String> findAllDistinctStatuses();
//    Page<EquipmentResponse> filter(Integer locationId, Integer categoryId, String name, String status, Pageable pageable);
    Page<EquipmentResponse> findEquipmentByLocationIdAndCategoryId(int categoryId,int locationId, Pageable pageable);
    Page<EquipmentResponse> findEquipmentByLocationId(int locationId, Pageable pageable);

}
