package com.example.backend.service.Impl;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Category;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.Location;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.EquipmentMapper;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.repository.LocationRepository;
import com.example.backend.service.EquipmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService{
    EquipmentMapper equipmentMapper;
    EquipmentRepository equipmentRepository;
    CategoryRepository categoryRepository;
    LocationRepository locationRepository;
    private Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }
    private Location getLocationById(int locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));
    }
    @Override
    public EquipmentResponse addEquipment(EquipmentRequest equipmentRequest) {
        boolean equipmentExists = equipmentRepository.existsByName(equipmentRequest.getName());
        if(equipmentExists) {
            throw new DuplicateResourceException("Equipment with name " + equipmentRequest.getName() + " already exists");
        }
        Equipment equipment = equipmentMapper.toEquipment(equipmentRequest);
        equipment.setCategory(getCategoryById(equipmentRequest.getCategoryId()));
        equipment.setLocation(getLocationById(equipmentRequest.getLocationId()));
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toEquipmentResponse(savedEquipment);
    }

    @Override
    public EquipmentResponse updateEquipment(int id, EquipmentRequest equipmentRequest) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        existingEquipment.setName(equipmentRequest.getName());
        existingEquipment.setStatus(equipmentRequest.getStatus());
        existingEquipment.setPurchaseDate(equipmentRequest.getPurchaseDate());
        existingEquipment.setDescription(equipmentRequest.getDescription());
        existingEquipment.setCategory(getCategoryById(equipmentRequest.getCategoryId()));
        existingEquipment.setLocation(getLocationById(equipmentRequest.getLocationId()));
        Equipment updatedEquipment = equipmentRepository.save(existingEquipment);
        return equipmentMapper.toEquipmentResponse(updatedEquipment);
    }

    @Override
    public EquipmentResponse getEquipmentById(int id) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        return equipmentMapper.toEquipmentResponse(equipment);
    }

    @Override
    public Page<EquipmentResponse> getAllEquipment(Pageable pageable) {
        Page<Equipment> getAll = equipmentRepository.findAll(pageable);
        return getAll.map(equipmentMapper::toEquipmentResponse);
    }

    @Override
    public void deleteEquipmentById(int id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public Page<EquipmentResponse> findEquipmentByLocationId(int locationId, Pageable pageable) {
        Page<Equipment> equipment = equipmentRepository.findByLocationId(locationId, pageable);
        if(equipment.isEmpty()){
            throw new ResourceNotFoundException("No equipment found for location id: " + locationId);
        }
        return equipment.map(equipmentMapper::toEquipmentResponse);
    }

    @Override
    public Page<EquipmentResponse> findEquipmentByCategoryId(int categoryId, Pageable pageable) {
        Page<Equipment> equipment = equipmentRepository.findByCategoryId(categoryId, pageable);
        if(equipment.isEmpty()){
            throw new ResourceNotFoundException("No equipment found for category id: " + categoryId);
        }
        return equipment.map(equipmentMapper::toEquipmentResponse);
    }

    @Override
    public long getTotalEquipment() {
        return equipmentRepository.getTotalEquipment();
    }

    @Override
    public Map<String, Long> countByStatus() {
        Map<String, Long> equipmentByStatus = new HashMap<>();
        equipmentByStatus.put("Active", equipmentRepository.countByStatus("Active"));
        equipmentByStatus.put("Broken", equipmentRepository.countByStatus("Broken"));
        equipmentByStatus.put("Maintenance", equipmentRepository.countByStatus("Maintenance"));
        return equipmentByStatus;
    }

}
