package com.example.backend.mapper;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
    Equipment toEquipment(EquipmentRequest equipmentRequest);
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "location.id", target = "locationId")
    EquipmentResponse toEquipmentResponse(Equipment equipment);
}
