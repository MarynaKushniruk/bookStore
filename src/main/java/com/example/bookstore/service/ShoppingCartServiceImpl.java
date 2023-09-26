package com.example.bookstore.service;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCard() {
        User authenticatedUser = userService.getAuthenticated();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(authenticatedUser.getId())
                .orElseGet(() -> registerNewCart(authenticatedUser));
        Long id = shoppingCart.getId();
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setId(id);
        shoppingCartResponseDto.setUserId(authenticatedUser.getId());
        shoppingCartResponseDto.setCartItems(cartItemService.getCartItemsByShoppingCartId(id));
        return shoppingCartResponseDto;
    }

    private ShoppingCart registerNewCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public CartItemResponseDto save(CartItemRequestDto cartItemRequestDto) {
        return cartItemService.save(cartItemRequestDto);
    }

    @Override
    public ShoppingCartResponseDto save(ShoppingCart shoppingCart) {
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }
}
