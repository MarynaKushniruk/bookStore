package com.example.bookstore.service;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;

public interface CartItemService {

    void delete(Long cartItemId);

    CartItemResponseDto addCartItem(String email, CartItemRequestDto cartItemRequestDto);

    CartItemResponseDto update(CartItemUpdateQuantityDto cartItemDto, Long id);
}
