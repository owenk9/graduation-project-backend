package com.example.backend.service;

import com.example.backend.dto.request.EquipmentItemRequest;
import com.example.backend.dto.response.EquipmentItemResponse;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.dto.response.StatusDistribution;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.EquipmentItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipmentItemService {
    EquipmentItemResponse addEquipmentItem(EquipmentItemRequest equipmentItemRequest);
    EquipmentItemResponse updateEquipmentItem(int id, EquipmentItemRequest equipmentItemRequest);
    void deleteEquipmentItem(int id);
    EquipmentItemResponse getEquipmentItemById(int id);
    long getTotalEquipmentItem();
    List<EquipmentItemResponse> getAllEquipmentItems();
    List<EquipmentItemResponse> getAllEquipmentItemByEquipmentId(int equipmentId);
    List<EquipmentItemResponse> getEquipmentItemByEquipmentIdAndLocationId(int equipmentId, int locationId);
    List<EquipmentItemResponse> getEquipmentItemByLocationId(int locationId);
    List<StatusDistribution> getStatusDistribution();
}
