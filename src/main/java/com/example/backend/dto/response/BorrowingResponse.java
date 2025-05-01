package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowingResponse {
    int id;
    int equipmentItemId;
    String serialNumber;
    String equipmentName;
    int usersId;
    LocalDateTime borrowDate;
    LocalDateTime returnDate;
    String status;
}
