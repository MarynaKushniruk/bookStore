package com.example.bookstore.service;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCard();

    CartItemResponseDto save(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto save(ShoppingCart shoppingCart);
}
