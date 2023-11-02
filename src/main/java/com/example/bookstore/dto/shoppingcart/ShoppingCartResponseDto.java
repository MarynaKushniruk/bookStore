package com.example.bookstore.dto.shoppingcart;

import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
