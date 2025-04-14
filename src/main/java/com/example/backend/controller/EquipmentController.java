package com.example.backend.controller;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.exception.InvalidRequestException;
import com.example.backend.service.EquipmentService;
import com.example.backend.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/equipment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EquipmentController {
    EquipmentService equipmentService;
    FileStorageService fileStorageService;

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EquipmentResponse> addEquipment(
            @RequestPart("equipment") @Valid EquipmentRequest equipmentRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image);
        }

        EquipmentResponse addEquipment = equipmentService.addEquipment(equipmentRequest, imageUrl);
        return ResponseEntity.status(201).body(addEquipment);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<EquipmentResponse>> getEquipment(@RequestParam(required = false) Integer locationId,
                                                                @RequestParam(required = false) Integer categoryId,
                                                                @RequestParam(required = false) String name,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EquipmentResponse> equipmentPage;
        if (locationId != null && categoryId != null) {
            throw new InvalidRequestException("Cannot provide both locationId and categoryId");
        } else if (name != null && (locationId != null || categoryId != null)) {
            throw new InvalidRequestException("Cannot combine name with locationId or categoryId");
        } else if (name != null) {
            equipmentPage = equipmentService.findEquipmentByName(name, pageable);
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

    @PatchMapping(value = "/update/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable int id,
            @RequestPart("equipment") @Valid EquipmentRequest equipmentRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image);
        }

        EquipmentResponse updateEquipment = equipmentService.updateEquipment(id, equipmentRequest, imageUrl);
        return ResponseEntity.ok(updateEquipment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable int id){
        equipmentService.deleteEquipmentById(id);
        return ResponseEntity.noContent().build();
    }


}