package com.example.backend.service.Impl;

import com.example.backend.dto.request.PermissionRequest;
import com.example.backend.dto.response.PermissionResponse;
import com.example.backend.entity.Permission;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.PermissionMapper;
import com.example.backend.repository.PermissionRepository;
import com.example.backend.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    PermissionMapper permissionMapper;
    PermissionRepository permissionRepository;

    @Override
    public PermissionResponse addPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse updatePermission(int id, PermissionRequest permissionRequest) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found permission with id " + id));
        permission.setPermissionName(permissionRequest.getPermissionName());
        permission.setDescription(permissionRequest.getDescription());
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(updatedPermission);
    }

    @Override
    public void deletePermission(int id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionResponse getPermissionById(int id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found permission with id " + id));
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public Page<PermissionResponse> getAllPermission(Pageable pageable) {
        Page<Permission> permission = permissionRepository.findAll(pageable);
        return permission.map(permissionMapper::toPermissionResponse);
    }
}
