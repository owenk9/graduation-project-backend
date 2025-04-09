package com.example.backend.dto.response;

import com.example.backend.entity.Equipment;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {
    int id;
    String categoryName;
    String description;
}
