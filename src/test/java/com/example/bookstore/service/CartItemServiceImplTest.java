package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import java.math.BigDecimal;
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
public class CartItemServiceImplTest {
    private static final long ID = 1;
    private static final String EMAIL = "jonh.doe@gmail.com";

    @InjectMocks
    private CartItemServiceImpl cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private BookRepository bookRepository;
    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private User user;
    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(ID);
        book.setTitle("Odyssey");
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
    }

    @AfterEach
    public void cleanUp() {
        book = null;
        user = null;
        shoppingCart = null;
        cartItem = null;
    }

    @Test
    void addCartItem_ValidCartItem_Ok() {
        CartItemRequestDto requestDto = createCartItemRequestDto();
        CartItemResponseDto expected = createCartItemDto();
        when(cartItemMapper.toModel(requestDto)).thenReturn(cartItem);
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(ID)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(expected);
        CartItemResponseDto actual = cartItemService.addCartItem(EMAIL, requestDto);
        assertEquals(expected, actual);
        verify(cartItemRepository, times(1)).save(cartItem);
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(bookRepository, times(1)).findById(ID);

    }

    @Test
    void update_ValidQuantity_Ok() {
        cartItem.setQuantity(4);
        when((cartItemRepository.findById(ID))).thenReturn(Optional.of(cartItem));
        CartItemResponseDto expected = createCartItemDto().setQuantity(4);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(expected);
        CartItemUpdateQuantityDto cartItemUpdateQuantityDto =
                new CartItemUpdateQuantityDto().setQuantity(4);
        CartItemResponseDto actual = cartItemService.update(cartItemUpdateQuantityDto, ID);
        assertEquals(expected, actual);
        verify(cartItemRepository, times(1)).findById(ID);
    }

    @Test
    void delete_ValidId_Ok() {
        cartItemService.delete(ID);
        verify(cartItemRepository, times(1)).deleteById(ID);
    }

    private CartItemRequestDto createCartItemRequestDto() {
        return new CartItemRequestDto()
                .setBookId(ID).setQuantity(2);
    }

    private CartItemResponseDto createCartItemDto() {
        return new CartItemResponseDto().setId(ID)
                .setBookId(ID).setBookTitle(book.getTitle()).setQuantity(2);
    }

    private ShoppingCartResponseDto createShoppingCartDto() {
        return new ShoppingCartResponseDto().setId(ID)
                .setCartItems(Set.of(createCartItemDto())).setUserId(ID);
    }

    private CartItem getCartItem() {
        return new CartItem()
                .setBook(new Book()
                        .setId(1L)
                        .setTitle("Title")
                        .setPrice(BigDecimal.TEN))
                .setQuantity(3)
                .setShoppingCart(new ShoppingCart()
                        .setId(1L));
    }
}
