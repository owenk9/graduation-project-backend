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
    String imageUrl;
    String categoryName;
    LocalDateTime purchaseDate;
    String description;
}
