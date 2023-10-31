package com.example.bookstore.service;

import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCardByUserEmail(String email);
}
