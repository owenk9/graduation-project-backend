package com.example.backend.controller;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.enums.BorrowingStatus;
import com.example.backend.exception.InvalidRequestException;
import com.example.backend.service.BorrowingService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrowing")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BorrowingController {
    BorrowingService borrowingService;
    @PostMapping("/request")
    public ResponseEntity<BorrowingResponse> requestBorrowing(@Valid @RequestBody BorrowingRequest request) {
        BorrowingResponse borrowingResponse = borrowingService.createBorrowing(request);
        return ResponseEntity.ok(borrowingResponse);
    }
    @GetMapping("/get")
    public ResponseEntity<Page<BorrowingResponse>> getBorrowing(
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowingResponse> borrowingPage = borrowingService.getAllBorrowings(pageable);
        return ResponseEntity.ok(borrowingPage);

    }
    @GetMapping("/get/pending")
    public ResponseEntity<Page<BorrowingResponse>> getPendingRequests(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowingResponse> borrowingPage = borrowingService.getPendingBorrowingList(pageable);
        return ResponseEntity.ok(borrowingPage);
    }
    @PatchMapping("/get/{id}/confirm")
    public ResponseEntity<BorrowingResponse> confirmBorrowing(@PathVariable int id,
                                                              @RequestParam BorrowingStatus status,
                                                              @RequestParam(required = false) String adminNote) {
        BorrowingResponse response = borrowingService.confirmBorrowing(id, status, adminNote);
        return ResponseEntity.ok(response);
    }
}
