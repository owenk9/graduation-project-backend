package com.example.backend.dto.response;

import com.example.backend.entity.Equipment;
import lombok.Data;

import java.util.List;

@Data
public class LocationResponse {
    int id;
    String locationName;
    List<EquipmentResponse> equipment;
}
