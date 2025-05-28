package com.example.backend.controller;

import com.example.backend.dto.request.BrokenRequest;
import com.example.backend.dto.response.BrokenResponse;
import com.example.backend.enums.BrokenStatus;
import com.example.backend.service.BrokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/broken")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BrokenController {
    BrokenService brokenService;

    @PostMapping("/report")
    public ResponseEntity<BrokenResponse> reportBroken(@RequestBody BrokenRequest brokenRequest) {
        return ResponseEntity.ok(brokenService.reportBroken(brokenRequest));
    }

    @GetMapping("/get/reports")
    public ResponseEntity<Page<BrokenResponse>> getAllBrokenReports(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BrokenResponse> broken = brokenService.getAllBrokenReports(pageable);
        return ResponseEntity.ok(broken);
    }
    @PatchMapping("/update_status/{id}")
    public ResponseEntity<BrokenResponse> updateBrokenStatus(
            @PathVariable int id,
            @RequestParam BrokenStatus status) {
        return ResponseEntity.ok(brokenService.updateBrokenStatus(id, status));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<BrokenResponse>> searchBrokenByEquipmentName(
            @RequestParam(required = false) String equipmentName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BrokenResponse> brokenPage = brokenService.searchBrokenByEquipmentName(equipmentName, pageable);
        return ResponseEntity.ok(brokenPage);
    }
}
