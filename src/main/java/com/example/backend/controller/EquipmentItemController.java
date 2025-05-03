package com.example.backend.controller;

import com.example.backend.dto.request.EquipmentItemRequest;
import com.example.backend.dto.response.EquipmentItemResponse;
import com.example.backend.service.EquipmentItemService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins = "*") // Added to ensure no CORS issues
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EquipmentItemController {
    EquipmentItemService equipmentItemService;

    @PostMapping("/add")
    public ResponseEntity<EquipmentItemResponse> addEquipmentItem(@Valid @RequestBody EquipmentItemRequest equipmentItemRequest) {
        EquipmentItemResponse equipmentItemResponse = equipmentItemService.addEquipmentItem(equipmentItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentItemResponse);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<EquipmentItemResponse> updateEquipmentItem(@PathVariable int id, @Valid @RequestBody EquipmentItemRequest equipmentItemRequest) {
        EquipmentItemResponse equipmentItemResponse = equipmentItemService.updateEquipmentItem(id, equipmentItemRequest);
        return ResponseEntity.status(HttpStatus.OK).body(equipmentItemResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEquipmentItem(@PathVariable int id) {
        equipmentItemService.deleteEquipmentItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted equipmentItem");
    }

    @GetMapping("/get")
    public ResponseEntity<List<EquipmentItemResponse>> getEquipmentItems(
            @RequestParam(required = false) Integer equipmentId,
            @RequestParam(required = false) Integer locationId) {

        List<EquipmentItemResponse> equipmentItemList;

        if (equipmentId != null && locationId != null) {
            equipmentItemList = equipmentItemService.getEquipmentItemByEquipmentIdAndLocationId(equipmentId, locationId);
        } else if (equipmentId != null) {
            equipmentItemList = equipmentItemService.getAllEquipmentItemByEquipmentId(equipmentId);
        } else if (locationId != null) {
            equipmentItemList = equipmentItemService.getEquipmentItemByLocationId(locationId);
        } else {
            equipmentItemList = equipmentItemService.getAllEquipmentItems();
        }

        return ResponseEntity.status(HttpStatus.OK).body(equipmentItemList);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<EquipmentItemResponse> getEquipmentItemById(@PathVariable int id) {
        EquipmentItemResponse response = equipmentItemService.getEquipmentItemById(id);
        return ResponseEntity.ok(response);
    }
}