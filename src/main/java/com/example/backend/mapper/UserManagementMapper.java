package com.example.backend.mapper;

import com.example.backend.dto.request.UserManagementRequest;
import com.example.backend.dto.response.UserManagementResponse;
import com.example.backend.entity.Role;
import com.example.backend.entity.UserRole;
import com.example.backend.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserManagementMapper {
    Users toUsers(UserManagementRequest userManagementRequest);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "userRoles", target = "role", qualifiedByName = "mapUserRolesToRole")


    UserManagementResponse toUserManagementResponse(Users users);
    @Named("mapUserRolesToRole")
    default String mapUserRolesToRole(Set<UserRole> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return null;
        }
        return userRoles.stream()
                .findFirst()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .map(Enum::name)
                .orElse(null);
    }
}
