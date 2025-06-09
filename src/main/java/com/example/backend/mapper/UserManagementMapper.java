package com.example.backend.mapper;

import com.example.backend.dto.request.UserManagementRequest;
import com.example.backend.dto.response.UserManagementResponse;
import com.example.backend.entity.Role;
import com.example.backend.entity.UserRole;
import com.example.backend.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
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
    default String mapUserRolesToRole(List<UserRole> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return null;
        }
        return userRoles.get(0)  // vì đã @OrderBy DESC nên cái mới nhất nằm đầu
                .getRole()
                .getRoleName()
                .name();
    }
}
