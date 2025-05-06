package com.example.backend.mapper;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.entity.Borrowing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {
    Borrowing toBorrowing(BorrowingRequest borrowingRequest);
    @Mapping(source = "users.lastName", target = "usersLastName")
    @Mapping(source = "users.firstName", target = "usersFirstName")
    @Mapping(source = "equipmentItem.equipment.name", target = "equipmentName")
    @Mapping(source = "equipmentItem.serialNumber", target = "serialNumber")
    BorrowingResponse toBorrowingResponse(Borrowing borrowing);
}
