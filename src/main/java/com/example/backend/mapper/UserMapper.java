package com.example.backend.mapper;

import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", expression = "java(users.getFirstName() + \" \" + users.getLastName())")
    @Mapping(target = "users.role.roleName")
    UserResponse toUserResponse(Users users);
}
