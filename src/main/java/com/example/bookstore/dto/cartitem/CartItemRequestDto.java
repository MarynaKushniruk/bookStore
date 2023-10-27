package com.example.bookstore.dto.cartitem;

import com.example.bookstore.model.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemRequestDto {
    @NotNull
    private Long id;
    @NotNull
    private User user;
    @Min(1)
    private Long bookId;
    @NotNull
    @Positive
    private int quantity;
}
