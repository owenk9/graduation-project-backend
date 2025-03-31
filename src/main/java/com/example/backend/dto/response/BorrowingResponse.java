package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowingResponse {
    int id;
    int equipmentId;
    int usersId;
    LocalDateTime borrowDate;
    LocalDateTime returnDate;
    String status;
}
