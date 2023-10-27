package com.example.bookstore.service;

import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCardByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can not find user by email" + email));
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getId())
                .orElseGet(() -> registerNewShoppingCart(user));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }
}
