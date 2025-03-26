package com.example.backend.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowingRequest {
    int equipmentId;
    int userId;
    LocalDateTime borrowDate;
    LocalDateTime returnDate;
    String status;

}
