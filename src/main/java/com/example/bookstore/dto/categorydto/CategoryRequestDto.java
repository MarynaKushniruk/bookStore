package com.example.bookstore.dto.categorydto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotNull
    private String name;
    private String description;
}
