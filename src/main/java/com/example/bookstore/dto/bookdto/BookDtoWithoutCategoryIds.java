package com.example.bookstore.dto.bookdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookDtoWithoutCategoryIds {
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
