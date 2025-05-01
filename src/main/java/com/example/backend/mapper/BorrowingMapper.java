package com.example.backend.mapper;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.entity.Borrowing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {
    Borrowing toBorrowing(BorrowingRequest borrowingRequest);
    @Mapping(source = "users.id", target = "usersId")
    @Mapping(source = "equipmentItem.id", target = "equipmentItemId")
    BorrowingResponse toBorrowingResponse(Borrowing borrowing);
}
