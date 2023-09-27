package com.example.bookstore.dto.cartitem;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long id;
    private String bookTitle;
    private Long bookId;
    private String author;
    private int quantity;

}
