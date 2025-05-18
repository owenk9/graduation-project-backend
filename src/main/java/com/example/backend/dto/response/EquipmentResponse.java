package com.example.backend.dto.response;

import lombok.Data;

@Data
public class EquipmentResponse {
    int id;
    String name;
    String imageUrl;
    String categoryName;
    String description;
}
