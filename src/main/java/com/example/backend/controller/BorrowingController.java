package com.example.backend.controller;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.exception.InvalidRequestException;
import com.example.backend.service.BorrowingService;
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
    @PostMapping("/add")
    public ResponseEntity<BorrowingResponse> addBorrowing(@RequestBody BorrowingRequest borrowingRequest) {
        BorrowingResponse addBorrowing = borrowingService.addBorrowing(borrowingRequest);
        return ResponseEntity.status(201).body(addBorrowing);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<BorrowingResponse> updateBorrowing(@PathVariable int id, @RequestBody BorrowingRequest borrowingRequest) {
        BorrowingResponse updateBorrowing = borrowingService.updateBorrowing(id, borrowingRequest);
        return ResponseEntity.ok(updateBorrowing);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBorrowing(@PathVariable int id) {
        borrowingService.deleteBorrowing(id);
        return ResponseEntity.status(200).body("Deleted Borrowing Successfully");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BorrowingResponse> getBorrowingById(@PathVariable int id) {
        BorrowingResponse borrowingResponse = borrowingService.getBorrowingById(id);
        return ResponseEntity.ok(borrowingResponse);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<BorrowingResponse>> getAllBorrowings(@RequestParam(required = false) Integer equipmentId,
                                                                    @RequestParam(required = false) Integer usersId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
       Pageable pageable = PageRequest.of(page, size);
       Page<BorrowingResponse> borrowingPage;
       if(equipmentId != null && usersId != null){
           throw new InvalidRequestException("Cannot provide both equipmentId and usersId");
       } else if(equipmentId != null){
           borrowingPage = borrowingService.findBorrowingByEquipmentItemId(equipmentId, pageable);
       } else if(usersId != null){
           borrowingPage = borrowingService.findBorrowingByUsersId(usersId, pageable);
       } else {
           borrowingPage = borrowingService.getAllBorrowings(pageable);
       }
       if(borrowingPage.isEmpty()){
           return ResponseEntity.noContent().build();
       }
       return ResponseEntity.ok(borrowingPage);
    }
}
