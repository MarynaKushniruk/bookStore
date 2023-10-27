package com.example.bookstore.service;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public void delete(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItemResponseDto update(CartItemUpdateQuantityDto cartItemDto, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't found "
                        + "cart bu id " + id));
        cartItem.setQuantity(cartItemDto.getQuantity());
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartItemResponseDto addCartItem(String email, CartItemRequestDto cartItemRequestDto) {
        User user = getUserByEmail(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> registerNewShoppingCart(user));
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(cartItemRequestDto.getBookId()))
                .findFirst();
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItemRequestDto.getQuantity());
        } else {
            cartItem = createNewCartItem(cartItemRequestDto, shoppingCart);
        }
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    private CartItem createNewCartItem(CartItemRequestDto cartItemRequestDto,
                                       ShoppingCart shoppingCart) {
        Book book = bookRepository
                .findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find book with id: "
                                + cartItemRequestDto.getBookId()));
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        return cartItem;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can not find user by email" + email));
    }

    private ShoppingCart registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }
}
