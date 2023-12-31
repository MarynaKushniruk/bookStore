package com.example.bookstore.controller;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.service.CartItemService;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
@PreAuthorize("hasRole('ROLE_USER')")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a cart item", description = "Add a cart item to the shopping cart")
    public ShoppingCartResponseDto addCartItemToShoppingCart(@RequestBody @Valid
                                                              CartItemRequestDto cartItemDto,
                                                              Authentication authentication) {
        cartItemService.addCartItem(authentication.getName(),cartItemDto);
        return shoppingCartService.getShoppingCardByUserEmail(authentication.getName());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a shopping cart", description =
            "Get a shopping cart of user")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        return shoppingCartService.getShoppingCardByUserEmail(authentication.getName());
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update a category", description =
            "Update a category by id")
    public ShoppingCartResponseDto updateQuantityOfItems(@RequestBody @Valid
                                                         CartItemUpdateQuantityDto
                                                                 cartItemDto,
                                                         @PathVariable Long cartItemId,
                                                         Authentication authentication) {
        cartItemService.update(cartItemDto, cartItemId);
        return shoppingCartService.getShoppingCardByUserEmail(authentication.getName());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete a cart item",
            description = "Delete a cart item from shopping cart by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ShoppingCartResponseDto deleteItemFromShoppingCart(
            @PathVariable Long cartItemId, Authentication authentication) {
        cartItemService.delete(cartItemId);
        return shoppingCartService.getShoppingCardByUserEmail(authentication.getName());
    }
}

