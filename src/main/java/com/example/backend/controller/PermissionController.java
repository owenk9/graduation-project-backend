package com.example.backend.controller;

import com.example.backend.dto.request.PermissionRequest;
import com.example.backend.dto.response.PermissionResponse;
import com.example.backend.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionController {
    PermissionService permissionService;
    @PostMapping("/add")
    public ResponseEntity<PermissionResponse> addPermission(@RequestBody PermissionRequest permissionRequest){
        PermissionResponse addPermission = permissionService.addPermission(permissionRequest);
        return ResponseEntity.status(201).body(addPermission);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable int id, @RequestBody PermissionRequest permissionRequest){
        PermissionResponse updatePermission = permissionService.updatePermission(id, permissionRequest);
        return ResponseEntity.ok(updatePermission);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PermissionResponse> getPermission(@PathVariable int id){
        PermissionResponse getPermission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(getPermission);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<PermissionResponse>> getAllPermission(@RequestParam int page,
                                                                     @RequestParam int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PermissionResponse> getAllPermission = permissionService.getAllPermission(pageable);
        return ResponseEntity.ok(getAllPermission);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePermissionById(@PathVariable int id){
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

}
