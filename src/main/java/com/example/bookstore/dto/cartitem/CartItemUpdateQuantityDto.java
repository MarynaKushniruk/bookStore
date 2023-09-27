package com.example.bookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemUpdateQuantityDto {
    @NotNull
    private int quantity;
}
