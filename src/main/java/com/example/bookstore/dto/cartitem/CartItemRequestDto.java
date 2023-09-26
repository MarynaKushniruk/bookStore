package com.example.bookstore.dto.cartitem;

import com.example.bookstore.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull
    private Long id;
    @NotNull
    private User user;
    @NotNull
    @Positive
    private int quantity;
}
