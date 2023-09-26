package com.example.bookstore.controller;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.service.CartItemService;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @PostMapping
    @Operation(summary = "Add cart item to shopping cart")
    private void addCartItemToShoppingCart(@RequestBody @Valid
                                               CartItemRequestDto cartItemDto) {
        cartItemService.setShoppingCartAndCartItems(cartItemDto.getUser(),
                cartItemMapper.toModel(cartItemDto));
    }

    @GetMapping
    @Operation(summary = "Get a shopping cart", description =
            "Get a shopping cart of user")
    public ShoppingCart getShoppingCart() {
        return shoppingCartMapper.toModel(
                shoppingCartService.getShoppingCard());
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update a category", description =
            "Update a category by id")
    public CartItemResponseDto updateQuantityOfItems(@RequestBody CartItemUpdateQuantityDto
                                                                 cartItemDto,
                                                     @PathVariable Long cartItemId) {
        return cartItemService.update(cartItemDto, cartItemId);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete a cart item",
            description = "Delete a cart item from shopping cart by id")
    public void deleteItemFromShoppingCart(@PathVariable Long cartItemId) {

        cartItemService.delete(cartItemId);
    }
}
