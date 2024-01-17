package com.example.bookstore.service;

import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final CartItemMapper mapper;

    @Transactional
    @Override
    public ShoppingCartResponseDto getShoppingCardByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can not find user by email" + email));
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser_Id(user.getId())
                .orElseGet(() -> registerNewShoppingCart(user));
        Long id = shoppingCart.getId();
        ShoppingCartResponseDto shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setId(id);
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setCartItems(shoppingCart.getCartItems()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet()));
        return shoppingCartDto;
    }

    @Override
    public void deleteById(Long id) {
        shoppingCartRepository.deleteById(id);
    }

    private ShoppingCart registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }
}
