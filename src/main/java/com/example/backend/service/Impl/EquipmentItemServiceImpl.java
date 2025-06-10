package com.example.backend.service.Impl;


import com.example.backend.dto.request.EquipmentItemRequest;
import com.example.backend.dto.response.EquipmentItemResponse;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.dto.response.StatusDistribution;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.EquipmentItem;
import com.example.backend.entity.Location;
import com.example.backend.enums.EquipmentItemStatus;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.EquipmentItemMapper;
import com.example.backend.mapper.EquipmentMapper;
import com.example.backend.repository.EquipmentItemRepository;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.repository.LocationRepository;
import com.example.backend.service.EquipmentItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EquipmentItemServiceImpl implements EquipmentItemService {
    EquipmentItemRepository equipmentItemRepository;
    EquipmentItemMapper equipmentItemMapper;
    LocationRepository locationRepository;
    EquipmentRepository equipmentRepository;
    EquipmentMapper equipmentMapper;
    private Location getLocationById(int locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));
    }
    private Equipment getEquipmentById(int equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentId));
    }
    @Override
    public EquipmentItemResponse addEquipmentItem(EquipmentItemRequest equipmentItemRequest) {
        if(equipmentItemRepository.existsBySerialNumber(equipmentItemRequest.getSerialNumber())){
            throw new DuplicateResourceException("Serial number already exists: " + equipmentItemRequest.getSerialNumber());
        }
        EquipmentItem equipmentItem = equipmentItemMapper.toEquipmentItem(equipmentItemRequest);
        equipmentItem.setEquipment(getEquipmentById(equipmentItemRequest.getEquipmentId()));
        equipmentItem.setLocation(getLocationById(equipmentItemRequest.getLocationId()));
        EquipmentItem equipmentItemSaved = equipmentItemRepository.save(equipmentItem);
        return equipmentItemMapper.toEquipmentItemResponse(equipmentItemSaved);
    }

    @Override
    public EquipmentItemResponse updateEquipmentItem(int id, EquipmentItemRequest equipmentItemRequest) {
        EquipmentItem equipmentItem = equipmentItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EquipmentItem not found with id: " + id));
        Optional<EquipmentItem> existingItem = equipmentItemRepository.findBySerialNumber(equipmentItemRequest.getSerialNumber());

        if (existingItem.isPresent() && existingItem.get().getId() != id) {
            throw new DuplicateResourceException("Serial number already exists: " + equipmentItemRequest.getSerialNumber());
        }
        equipmentItem.setSerialNumber(equipmentItemRequest.getSerialNumber());
        equipmentItem.setStatus(EquipmentItemStatus.valueOf(equipmentItemRequest.getStatus()));
        equipmentItem.setPurchaseDate(equipmentItemRequest.getPurchaseDate());
        equipmentItem.setLocation(getLocationById(equipmentItemRequest.getLocationId()));
        EquipmentItem equipmentItemSaved = equipmentItemRepository.save(equipmentItem);
        return equipmentItemMapper.toEquipmentItemResponse(equipmentItemSaved);
    }

    @Override
    public void deleteEquipmentItem(int id) {
        equipmentItemRepository.deleteById(id);
    }

    @Override
    public EquipmentItemResponse getEquipmentItemById(int id) {
        EquipmentItem equipmentItem = equipmentItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EquipmentItem not found with id: " + id));
        return equipmentItemMapper.toEquipmentItemResponse(equipmentItem);
    }

    @Override
    public long getTotalEquipmentItem() {
        return equipmentItemRepository.getTotalEquipmentItem();
    }

    @Override
    public List<EquipmentItemResponse> getAllEquipmentItems() {
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        return equipmentItemList.stream().map(equipmentItemMapper::toEquipmentItemResponse).collect(Collectors.toList());
    }

    @Override
    public List<EquipmentItemResponse> getAllEquipmentItemByEquipmentId(int equipmentId) {
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAllEquipmentItemByEquipmentId(equipmentId);
        return equipmentItemList.stream().map(equipmentItemMapper::toEquipmentItemResponse).collect(Collectors.toList());
    }

    @Override
    public List<EquipmentItemResponse> getEquipmentItemByEquipmentIdAndLocationId(int equipmentId, int locationId) {
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findEquipmentItemByEquipmentIdAndLocationId(equipmentId, locationId);
        return equipmentItemList.stream().map(equipmentItemMapper::toEquipmentItemResponse).collect(Collectors.toList());
    }

    @Override
    public List<EquipmentItemResponse> getEquipmentItemByLocationId(int locationId) {
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findEquipmentItemByLocationId(locationId);
        return equipmentItemList.stream().map(equipmentItemMapper::toEquipmentItemResponse).collect(Collectors.toList());
    }

    @Override
    public List<StatusDistribution> getStatusDistribution() {
        List<Object[]> rawResults = equipmentItemRepository.countByStatusRaw();
        return rawResults.stream()
                .map(result -> new StatusDistribution(
                        result[0] != null ? ((EquipmentItemStatus) result[0]).toString() : null,
                        (Long) result[1]
                ))
                .collect(Collectors.toList());
    }
}
