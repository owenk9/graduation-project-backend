package com.example.backend.mapper;

import com.example.backend.dto.request.EquipmentItemRequest;
import com.example.backend.dto.response.EquipmentItemResponse;
import com.example.backend.entity.EquipmentItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentItemMapper {
    EquipmentItem toEquipmentItem(EquipmentItemRequest equipmentItemRequest);
    @Mapping(source = "location.locationName", target = "locationName")
    @Mapping(source = "equipment.id", target = "equipmentId")
    EquipmentItemResponse toEquipmentItemResponse(EquipmentItem equipmentItem);
}
