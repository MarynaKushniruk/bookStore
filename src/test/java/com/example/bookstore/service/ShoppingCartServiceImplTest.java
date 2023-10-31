package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShoppingCartServiceImplTest {
    private static final String EMAIL = "ivan.zhuk@gmail.com";
    private static final long ID = 1;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private User user;
    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(ID);
        book.setTitle("Book 1");
        user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        cartItem = new CartItem();
        cartItem.setId(ID);
        cartItem.setQuantity(2);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.setCartItems(Set.of(cartItem));
    }

    @AfterEach
    public void cleanUp() {
        book = null;
        user = null;
        shoppingCart = null;
        cartItem = null;
    }

    @Test
    public void getShoppingCardByUserEmail_WithValidEmail_shouldReturnShoppingCart() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findById(ID)).thenReturn(Optional.of(shoppingCart));
        ShoppingCartResponseDto expected = createShoppingCartDto();
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartService.getShoppingCardByUserEmail(EMAIL);
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(shoppingCartRepository, times(1)).findById(ID);
    }

    private CartItemResponseDto createCartItemDto() {
        return new CartItemResponseDto().setId(ID)
                .setBookId(ID).setBookTitle(book.getTitle()).setQuantity(2);
    }

    private ShoppingCartResponseDto createShoppingCartDto() {
        return new ShoppingCartResponseDto().setId(ID)
                .setCartItems(Set.of(createCartItemDto())).setUserId(ID);
    }
}
