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
import java.util.List;

@RestController
@RequestMapping("/equipment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EquipmentController {
    EquipmentService equipmentService;
    FileStorageService fileStorageService;

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EquipmentResponse> addEquipment(
            @RequestPart("equipment") EquipmentRequest equipmentRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image);
        }

        EquipmentResponse addEquipment = equipmentService.addEquipment(equipmentRequest, imageUrl);
        return ResponseEntity.status(201).body(addEquipment);
    }

//    @GetMapping("/get")
//    public ResponseEntity<Page<EquipmentResponse>> getEquipment(@RequestParam(required = false) Integer locationId,
//                                                                @RequestParam(required = false) Integer categoryId,
//                                                                @RequestParam(required = false) String name,
//                                                                @RequestParam(required = false) String status,
//                                                                @RequestParam(defaultValue = "0") int page,
//                                                                @RequestParam(defaultValue = "10") int size){
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        Pageable pageable = PageRequest.of(page, size, sort);
//        Page<EquipmentResponse> equipmentPage = equipmentService.filter(locationId, categoryId, name, status, pageable);
//        return ResponseEntity.ok(equipmentPage);
//    }

//    @GetMapping("/get-statuses")
//    public ResponseEntity<List<String>> getAllStatuses() {
//        List<String> statuses = equipmentService.findAllDistinctStatuses();
//        return ResponseEntity.ok(statuses);
//    }
    @GetMapping("/get")
    public ResponseEntity<Page<EquipmentResponse>> getAllEquipment(Pageable pageable) {
        Page<EquipmentResponse> equipmentResponse = equipmentService.getAllEquipment(pageable);
        return ResponseEntity.status(200).body(equipmentResponse);
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