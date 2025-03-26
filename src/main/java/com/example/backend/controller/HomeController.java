package com.example.backend.controller;

import com.example.backend.service.BorrowingService;
import com.example.backend.service.EquipmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/home")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HomeController {
    EquipmentService equipmentService;
    BorrowingService borrowingService;
    @GetMapping("/total-equipment")
    public ResponseEntity<Long> getTotalEquipment() {
        return ResponseEntity.ok(equipmentService.getTotalEquipment());
    }
    @GetMapping("/equipment-count")
    public ResponseEntity<Map<String, Long>> countByStatus () {
        return ResponseEntity.ok(equipmentService.countByStatus());
    }
    @GetMapping("/total-borrowing")
    public ResponseEntity<Long> getTotalBorrowings() {
        return ResponseEntity.ok(borrowingService.getTotalBorrowings());
    }
}
