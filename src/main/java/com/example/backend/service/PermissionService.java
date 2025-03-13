package com.example.backend.service;

import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.request.PermissionRequest;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.dto.response.PermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    PermissionResponse addPermission(PermissionRequest permissionRequest);
    PermissionResponse updatePermission(int id, PermissionRequest permissionRequest);
    void deletePermission(int id);
    PermissionResponse getPermissionById(int id);
    Page<PermissionResponse> getAllPermission(Pageable pageable);
}
