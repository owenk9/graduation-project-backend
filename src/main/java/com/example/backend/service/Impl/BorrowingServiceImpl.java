package com.example.backend.service.Impl;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.entity.Borrowing;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.EquipmentItem;
import com.example.backend.entity.Users;
import com.example.backend.enums.BorrowingStatus;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.BorrowingMapper;
import com.example.backend.repository.BorrowingRepository;
import com.example.backend.repository.EquipmentItemRepository;
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
    EquipmentItemRepository equipmentItemRepository;
    UsersRepository usersRepository;
    private Equipment getEquipmentById(int equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentId));
    }
    private Users getUserById(int userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
    private EquipmentItem getEquipmentItemById(int equipmentItemId) {
        return equipmentItemRepository.findById(equipmentItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment item not found with id: " + equipmentItemId));
    }
    @Override
    public BorrowingResponse addBorrowing(BorrowingRequest borrowingRequest) {
        Borrowing borrowing = borrowingMapper.toBorrowing(borrowingRequest);
        borrowing.setEquipmentItem(getEquipmentItemById(borrowingRequest.getEquipmentItemId()));
        borrowing.setUsers(getUserById(borrowingRequest.getUsersId()));
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
        return borrowingMapper.toBorrowingResponse(savedBorrowing);
    }

    @Override
    public BorrowingResponse updateBorrowing(int id, BorrowingRequest borrowingRequest) {
        Borrowing existingBorrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
        existingBorrowing.setBorrowDate(borrowingRequest.getBorrowDate());
        existingBorrowing.setReturnDate(borrowingRequest.getReturnDate());
        existingBorrowing.setEquipmentItem(getEquipmentItemById(borrowingRequest.getEquipmentItemId()));
        existingBorrowing.setUsers(getUserById(borrowingRequest.getUsersId()));
        existingBorrowing.setStatus(BorrowingStatus.valueOf(borrowingRequest.getStatus()));
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
    public Page<BorrowingResponse> findBorrowingByUsersId(int usersId, Pageable pageable) {
        Page<Borrowing> borrowing = borrowingRepository.findBorrowingByUsersId(usersId, pageable);
        if(borrowing.isEmpty()){
            throw new ResourceNotFoundException("No borrowing found for user id: " + usersId);
        }
        return borrowing.map(borrowingMapper::toBorrowingResponse);
    }

    @Override
    public Page<BorrowingResponse> findBorrowingByEquipmentItemId(int equipmentItemId, Pageable pageable) {
        Page<Borrowing> borrowing = borrowingRepository.findBorrowingByEquipmentItemId(equipmentItemId, pageable);
        if(borrowing.isEmpty()){
            throw new ResourceNotFoundException("No borrowing found for equipment id: " + equipmentItemId);
        }
        return borrowing.map(borrowingMapper::toBorrowingResponse);
    }

    @Override
    public long getTotalBorrowings() {
        return borrowingRepository.getTotalBorrowings();
    }
}
