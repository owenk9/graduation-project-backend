package com.example.backend.mapper;

import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.request.PermissionRequest;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.entity.Location;
import com.example.backend.entity.RolePermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    RolePermission toRolePermission(PermissionRequest permissionRequest);
    RolePermission toLocationResponse(Location location);
}
