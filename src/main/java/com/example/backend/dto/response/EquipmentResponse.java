package com.example.backend.dto.response;

import com.example.backend.entity.Borrowing;
import com.example.backend.entity.Maintenance;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EquipmentResponse {
    int id;
    String name;
    int quantity;
    int categoryId;
    String status;
    int locationId;
    LocalDateTime purchaseDate;
    String description;
}
