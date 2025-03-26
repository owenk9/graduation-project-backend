package com.example.backend.mapper;

import com.example.backend.dto.request.MaintenanceRequest;
import com.example.backend.dto.response.MaintenanceResponse;
import com.example.backend.entity.Maintenance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface MaintenanceMapper {
    Maintenance toMaintenance(MaintenanceRequest maintenanceRequest);
    @Mapping(source = "equipment.id", target = "equipmentId")
    MaintenanceResponse toMaintenanceResponse(Maintenance maintenance);
}
