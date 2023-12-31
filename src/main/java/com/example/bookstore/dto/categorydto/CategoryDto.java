package com.example.bookstore.dto.categorydto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryDto {
    private Long id;
    @NotNull
    private String name;
    private String description;
}
