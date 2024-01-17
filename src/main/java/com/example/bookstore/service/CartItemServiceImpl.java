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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public CartItemResponseDto addCartItem(String email, CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = createNewCartItem(cartItemRequestDto);
        User user = getUserByEmail(email);
        setShoppingCartAndCartItems(user, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public Set<CartItemResponseDto> findByShoppingCartId(Long id) {
        return cartItemRepository.findCartItemsByShoppingCartId(id).stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void setShoppingCartAndCartItems(User user, CartItem cartItem) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser_Id(user.getId())
                .orElseGet(() -> registerNewShoppingCart(user));
        cartItem.setShoppingCart(shoppingCart);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        if (shoppingCart.getCartItems().isEmpty()) {
            shoppingCart.setCartItems(cartItems);
        } else {
            shoppingCart.getCartItems().add(cartItem);
        }
    }

    private ShoppingCart registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    private CartItem createNewCartItem(CartItemRequestDto cartItemRequestDto) {
        Book book = bookRepository
                .findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find book with id: "
                                + cartItemRequestDto.getBookId()));
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setBook(book);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        return cartItem;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can not find user by email" + email));
    }
}
