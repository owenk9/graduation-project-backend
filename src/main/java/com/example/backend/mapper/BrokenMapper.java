package com.example.backend.mapper;

import com.example.backend.dto.request.BrokenRequest;
import com.example.backend.dto.response.BrokenResponse;
import com.example.backend.entity.Broken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrokenMapper {

    Broken toBroken(BrokenRequest brokenRequest);
    @Mapping(source = "equipmentItem.equipment.name", target = "equipmentName")
    @Mapping(source = "equipmentItem.serialNumber", target = "serialNumber")
    @Mapping(target = "fullName", expression = "java(broken.getUsers().getFirstName() + \" \" + broken.getUsers().getLastName())")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "equipmentItem.location.locationName", target = "locationName")
    BrokenResponse toBrokenResponse(Broken broken);
}
