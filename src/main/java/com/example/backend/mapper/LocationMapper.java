package com.example.backend.mapper;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.entity.Equipment;
import com.example.backend.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = EquipmentMapper.class)
public interface LocationMapper {
    Location toLocation(LocationRequest locationRequest);
    LocationResponse toLocationResponse(Location location);
}
