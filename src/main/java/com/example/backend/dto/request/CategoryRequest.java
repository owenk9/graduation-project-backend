package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotNull(message = "Category name cannot be null")
    String categoryName;
    String description;

}
