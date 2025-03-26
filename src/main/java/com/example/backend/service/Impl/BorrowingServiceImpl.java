package com.example.backend.service.Impl;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.entity.Borrowing;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.Users;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.BorrowingMapper;
import com.example.backend.repository.BorrowingRepository;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.repository.UsersRepository;
import com.example.backend.service.BorrowingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {
    BorrowingMapper borrowingMapper;
    BorrowingRepository borrowingRepository;
    EquipmentRepository equipmentRepository;
    UsersRepository usersRepository;
    private Equipment getEquipmentById(int equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentId));
    }
    private Users getUserById(int userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
    @Override
    public BorrowingResponse addBorrowing(BorrowingRequest borrowingRequest) {
        Borrowing borrowing = borrowingMapper.toBorrowing(borrowingRequest);
        borrowing.setEquipment(getEquipmentById(borrowingRequest.getEquipmentId()));
        borrowing.setUsers(getUserById(borrowingRequest.getUserId()));
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
        return borrowingMapper.toBorrowingResponse(savedBorrowing);
    }

    @Override
    public BorrowingResponse updateBorrowing(int id, BorrowingRequest borrowingRequest) {
        Borrowing existingBorrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
        existingBorrowing.setBorrowDate(borrowingRequest.getBorrowDate());
        existingBorrowing.setReturnDate(borrowingRequest.getReturnDate());
        existingBorrowing.setEquipment(getEquipmentById(borrowingRequest.getEquipmentId()));
        existingBorrowing.setUsers(getUserById(borrowingRequest.getUserId()));
        existingBorrowing.setStatus(borrowingRequest.getStatus());
        Borrowing savedBorrowing = borrowingRepository.save(existingBorrowing);
        return borrowingMapper.toBorrowingResponse(savedBorrowing);
    }

    @Override
    public void deleteBorrowing(int id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public BorrowingResponse getBorrowingById(int id) {
        Borrowing borrowing = borrowingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
        return borrowingMapper.toBorrowingResponse(borrowing);
    }

    @Override
    public Page<BorrowingResponse> getAllBorrowings(Pageable pageable) {
        Page<Borrowing> borrowings = borrowingRepository.findAll(pageable);
        return borrowings.map(borrowingMapper::toBorrowingResponse);
    }

    @Override
    public long getTotalBorrowings() {
        return borrowingRepository.getTotalBorrowings();
    }
}
