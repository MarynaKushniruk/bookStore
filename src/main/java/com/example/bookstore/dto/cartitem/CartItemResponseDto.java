package com.example.bookstore.dto.cartitem;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CartItemResponseDto {
    private Long id;
    private String bookTitle;
    private Long bookId;
    private String author;
    private int quantity;

}
