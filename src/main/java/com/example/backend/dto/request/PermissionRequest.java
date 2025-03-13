package com.example.backend.dto.request;


import jakarta.validation.constraints.NotNull;

public class PermissionRequest {
    @NotNull(message = "Permission name cannot be null")
    String permissionName;
    String description;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
