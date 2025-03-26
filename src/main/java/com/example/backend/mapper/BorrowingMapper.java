package com.example.backend.mapper;

import com.example.backend.dto.request.BorrowingRequest;
import com.example.backend.dto.response.BorrowingResponse;
import com.example.backend.entity.Borrowing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {
    Borrowing toBorrowing(BorrowingRequest borrowingRequest);
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    BorrowingResponse toBorrowingResponse(Borrowing borrowing);
}
