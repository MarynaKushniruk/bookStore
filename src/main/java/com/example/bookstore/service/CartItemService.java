package com.example.bookstore.service;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.User;
import java.util.Set;

public interface CartItemService {
    CartItemResponseDto save(CartItemRequestDto cartItemRequestDto);

    void delete(Long cartItemId);

    Set<CartItemResponseDto> getCartItemsByShoppingCartId(Long shoppingCartId);

    void setShoppingCartAndCartItems(User user, CartItem cartItem);

    CartItemResponseDto update(CartItemUpdateQuantityDto cartItemDto, Long id);
}
