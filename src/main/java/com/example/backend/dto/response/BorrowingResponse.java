package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowingResponse {
    int id;
    String serialNumber;
    String equipmentName;
    String usersFirstName;
    String usersLastName;
    String note;
    String adminNote;
    LocalDateTime borrowDate;
    LocalDateTime returnDate;
    String status;
}
