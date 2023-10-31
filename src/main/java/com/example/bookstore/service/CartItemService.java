package com.example.bookstore.service;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;

public interface CartItemService {
    CartItemResponseDto addCartItem(String email, CartItemRequestDto cartItemRequestDto);

    void delete(Long cartItemId);

    CartItemResponseDto update(CartItemUpdateQuantityDto cartItemDto, Long id);
}
