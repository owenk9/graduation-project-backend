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
import com.example.backend.service.FileStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    EquipmentMapper equipmentMapper;
    EquipmentRepository equipmentRepository;
    CategoryRepository categoryRepository;
    LocationRepository locationRepository;
    FileStorageService fileStorageService;

    private Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

    private Location getLocationById(int locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));
    }

    @Override
    public EquipmentResponse addEquipment(EquipmentRequest equipmentRequest, String imageUrl) {
        boolean equipmentExists = equipmentRepository.existsByName(equipmentRequest.getName());
        if(equipmentExists) {
            throw new DuplicateResourceException("Equipment with name " + equipmentRequest.getName() + " already exists");
        }
        Equipment equipment = equipmentMapper.toEquipment(equipmentRequest);
        equipment.setCategory(getCategoryById(equipmentRequest.getCategoryId()));
        equipment.setImageUrl(imageUrl);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toEquipmentResponse(savedEquipment);
    }

    @Override
    @Transactional
    public EquipmentResponse updateEquipment(int id, EquipmentRequest equipmentRequest, String imageUrl) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        existingEquipment.setName(equipmentRequest.getName());
        existingEquipment.setDescription(equipmentRequest.getDescription());
        existingEquipment.setCategory(getCategoryById(equipmentRequest.getCategoryId()));
        if (imageUrl != null) {
            // Delete the old image if it exists
            String oldImageUrl = existingEquipment.getImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                try {
                    String fileName = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);
                    fileStorageService.deleteFile(fileName);
                } catch (IOException e) {
                    // Log the exception but continue with the update
                    System.err.println("Failed to delete old image: " + e.getMessage());
                }
            }
            existingEquipment.setImageUrl(imageUrl);
        }

        Equipment updatedEquipment = equipmentRepository.save(existingEquipment);
        return equipmentMapper.toEquipmentResponse(updatedEquipment);
    }

    @Override
    public EquipmentResponse getEquipmentById(int id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
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
    public Page<EquipmentResponse> findEquipmentByCategoryId(int categoryId, Pageable pageable) {
        Page<Equipment> equipment = equipmentRepository.findEquipmentByCategoryId(categoryId, pageable);
        return equipment.map(equipmentMapper::toEquipmentResponse);
    }

    @Override
    public long getTotalEquipment() {
        return equipmentRepository.getTotalEquipment();
    }

    @Override
    public Page<EquipmentResponse> findEquipmentByName(String name, Pageable pageable) {
        Page<Equipment> equipment = equipmentRepository.findByNameContainingIgnoreCase(name, pageable);
        return equipment.map(equipmentMapper::toEquipmentResponse);
    }

    @Override
    public Page<EquipmentResponse> findEquipmentByLocationIdAndCategoryId(int categoryId, int locationId, Pageable pageable) {
        Page<Equipment> equipment = equipmentRepository.findEquipmentByCategoryIdAndLocationId(categoryId, locationId, pageable);
        return equipment.map(equipmentMapper::toEquipmentResponse);
    }

    @Override
    public Page<EquipmentResponse> findEquipmentByLocationId(int locationId, Pageable pageable) {
        Page<Equipment> equipment = equipmentRepository.findEquipmentByLocationId(locationId, pageable);
        return equipment.map(equipmentMapper::toEquipmentResponse);
    }
}