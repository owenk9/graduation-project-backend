package com.example.backend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BorrowingConfirmationResponse {
    int borrowingId;
    String serialNumber;
    String equipmentName;
    String usersFirstName;
    String usersLastName;
    String note;
    String adminNote;
    LocalDateTime borrowDate;
    LocalDateTime returnDate;
    String status;
    int notificationId;
    String message;
    boolean isRead;
    LocalDateTime createdAt;

    public BorrowingConfirmationResponse(BorrowingResponse borrowingResponse, NotificationResponse notificationResponse) {
    }
}
