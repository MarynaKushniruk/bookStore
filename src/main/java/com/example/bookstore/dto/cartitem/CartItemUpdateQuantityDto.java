package com.example.bookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemUpdateQuantityDto {
    @NotNull
    private int quantity;
}
