package com.example.backend.service.Impl;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.entity.Borrowing;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.EquipmentItem;
import com.example.backend.entity.Users;
import com.example.backend.enums.BorrowingStatus;
import com.example.backend.enums.EquipmentItemStatus;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.BorrowingMapper;
import com.example.backend.repository.BorrowingRepository;
import com.example.backend.repository.EquipmentItemRepository;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.repository.UsersRepository;
import com.example.backend.service.BorrowingService;
import com.example.backend.service.NotificationService;
import jakarta.transaction.Transactional;
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
    NotificationService notificationService;
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
//    private String getStatusByEquipmentItemId(int equipmentItemId) {
//
//    }
//    @Override
//    public BorrowingResponse addBorrowing(BorrowingRequest borrowingRequest) {
//        Borrowing borrowing = borrowingMapper.toBorrowing(borrowingRequest);
//        borrowing.setEquipmentItem(getEquipmentItemById(borrowingRequest.getEquipmentItemId()));
//        borrowing.setUsers(getUserById(borrowingRequest.getUsersId()));
//        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
//        return borrowingMapper.toBorrowingResponse(savedBorrowing);
//    }
//
//    @Override
//    public BorrowingResponse updateBorrowing(int id, BorrowingRequest borrowingRequest) {
//        Borrowing existingBorrowing = borrowingRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
//        existingBorrowing.setBorrowDate(borrowingRequest.getBorrowDate());
//        existingBorrowing.setReturnDate(borrowingRequest.getReturnDate());
//        existingBorrowing.setEquipmentItem(getEquipmentItemById(borrowingRequest.getEquipmentItemId()));
//        existingBorrowing.setUsers(getUserById(borrowingRequest.getUsersId()));
//        existingBorrowing.setStatus(BorrowingStatus.valueOf(borrowingRequest.getStatus()));
//        Borrowing savedBorrowing = borrowingRepository.save(existingBorrowing);
//        return borrowingMapper.toBorrowingResponse(savedBorrowing);
//    }
//
//    @Override
//    public void deleteBorrowing(int id) {
//        equipmentRepository.deleteById(id);
//    }
//
//    @Override
//    public BorrowingResponse getBorrowingById(int id) {
//        Borrowing borrowing = borrowingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
//        return borrowingMapper.toBorrowingResponse(borrowing);
//    }
//
    @Override
    public Page<BorrowingResponse> getAllBorrowings(Pageable pageable) {
        Page<Borrowing> borrowings = borrowingRepository.findAll(pageable);
        return borrowings.map(borrowingMapper::toBorrowingResponse);
    }
//
//    @Override
//    public Page<BorrowingResponse> findBorrowingByUsersId(int usersId, Pageable pageable) {
//        Page<Borrowing> borrowing = borrowingRepository.findBorrowingByUsersId(usersId, pageable);
//        if(borrowing.isEmpty()){
//            throw new ResourceNotFoundException("No borrowing found for user id: " + usersId);
//        }
//        return borrowing.map(borrowingMapper::toBorrowingResponse);
//    }
//
//    @Override
//    public Page<BorrowingResponse> findBorrowingByEquipmentItemId(int equipmentItemId, Pageable pageable) {
//        Page<Borrowing> borrowing = borrowingRepository.findBorrowingByEquipmentItemId(equipmentItemId, pageable);
//        if(borrowing.isEmpty()){
//            throw new ResourceNotFoundException("No borrowing found for equipment id: " + equipmentItemId);
//        }
//        return borrowing.map(borrowingMapper::toBorrowingResponse);
//    }
//
//    @Override
//    public long getTotalBorrowings() {
//        return borrowingRepository.getTotalBorrowings();
//    }

    @Override
    public BorrowingResponse createBorrowing(BorrowingRequest borrowingRequest) {
        EquipmentItem equipmentItem = equipmentItemRepository.findById(borrowingRequest.getEquipmentItemId())
                .orElseThrow(() -> new ResourceNotFoundException("EquipmentItem not found with id: " + borrowingRequest.getEquipmentItemId()));
        Users users = usersRepository.findById(borrowingRequest.getUsersId())
                .orElseThrow(() -> new ResourceNotFoundException("Users not found with id: " + borrowingRequest.getUsersId()));
        if(!EquipmentItemStatus.ACTIVE.equals(equipmentItem.getStatus())) {
            throw new RuntimeException("Equipment item not ready to borrow");
        }
        Borrowing borrowing = borrowingMapper.toBorrowing(borrowingRequest);
        borrowing.setEquipmentItem(equipmentItem);
        borrowing.setUsers(users);
        borrowing.setStatus(BorrowingStatus.PENDING);
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
        return borrowingMapper.toBorrowingResponse(savedBorrowing);
    }
    @Transactional
    @Override
    public Page<BorrowingResponse> getPendingBorrowingList(Pageable pageable) {
        Page<Borrowing> borrowings  = borrowingRepository.findPendingWithAssociations(pageable);
        return borrowings.map(borrowingMapper::toBorrowingResponse);
    }
    @Transactional
    @Override
    public BorrowingResponse confirmBorrowing(int id, BorrowingStatus borrowingStatus, String adminNote) {
        if(borrowingStatus != BorrowingStatus.APPROVED && borrowingStatus != BorrowingStatus.REJECTED){
            throw new ResourceNotFoundException("Status must be APPROVED or REJECTED");
        }
        Borrowing borrowing = borrowingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
        if(borrowing.getStatus() != BorrowingStatus.PENDING){
            throw new ResourceNotFoundException("Request has been processed before");
        }
        borrowing.setStatus(borrowingStatus);
        if(adminNote != null){
            borrowing.setAdminNote(adminNote);
        }
        if(borrowingStatus == BorrowingStatus.APPROVED){
           EquipmentItem equipmentItem = borrowing.getEquipmentItem();
           equipmentItem.setStatus(EquipmentItemStatus.BORROWED);
           equipmentItemRepository.save(equipmentItem);
        }
        Borrowing updatedBorrowing = borrowingRepository.save(borrowing);
        String message;
        if (borrowingStatus == BorrowingStatus.APPROVED) {
            message = "Borrow request " + borrowing.getEquipmentItem().getEquipment().getName() + " của bạn đã được duyệt.";
        } else {
            message = "Borrow request " + borrowing.getEquipmentItem().getEquipment().getName() + " của bạn đã bị từ chối. Lý do: " + (adminNote != null ? adminNote : "Không có lý do cụ thể.");
        }
        notificationService.sendNotification(borrowing.getUsers().getId(), message);
        return borrowingMapper.toBorrowingResponse(updatedBorrowing);
    }



}
