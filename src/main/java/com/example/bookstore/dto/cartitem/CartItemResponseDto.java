package com.example.bookstore.dto.cartitem;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemResponseDto {
    private Long id;
    private String bookTitle;
    private Long bookId;
    private String author;
    private int quantity;

}
