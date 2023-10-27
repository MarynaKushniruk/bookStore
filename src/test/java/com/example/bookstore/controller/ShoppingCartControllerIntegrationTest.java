package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateQuantityDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerIntegrationTest {
    protected static MockMvc mockMvc;
    private static final long ID = 1;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        teardown(dataSource);
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/delete-for-shopping_cart-cart_item-tests.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    @Sql(scripts = "classpath:database/add-for-shopping_cart-cart_item-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getShoppingCart_returnShoppingCartResponseDto_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(ID, actual.getUserId());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    @Sql(scripts = "classpath:database/add-for-method-add-cart-item-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addCartItemToShoppingCart_returnShoppingCartResponseDto_Ok() throws Exception {
        CartItemRequestDto requestDto = createCartItemRequestDto();
        CartItemResponseDto expected = createCartItemDto();
        String request = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(post("/cart")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(actual.getCartItems().contains(expected));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    @DisplayName("Update books quantity in shopping cart")
    @Sql(scripts = "classpath:database/add-for-shopping_cart-cart_item-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateQuantityOfItems_ValidQuantity_Ok() throws Exception {
        CartItemUpdateQuantityDto updateRequest = new CartItemUpdateQuantityDto()
                .setQuantity(8);
        CartItemResponseDto expected = createCartItemDto().setQuantity(8);
        String request = objectMapper.writeValueAsString(updateRequest);
        MvcResult mvcResult = mockMvc.perform(put("/cart/cart-items/1")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertTrue(actual.getCartItems().contains(expected));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    @Sql(scripts = "classpath:database/add-for-shopping_cart-cart_item-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteItemFromShoppingCart_Ok() throws Exception {
        mockMvc.perform(delete("/cart/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private CartItemRequestDto createCartItemRequestDto() {
        return new CartItemRequestDto()
                .setBookId(ID).setQuantity(2);
    }

    private CartItemResponseDto createCartItemDto() {
        return new CartItemResponseDto().setId(ID)
                .setBookId(ID).setBookTitle("Book 1").setQuantity(2);
    }

    private ShoppingCartResponseDto createShoppingCartDto() {
        return new ShoppingCartResponseDto().setId(ID)
                .setCartItems(Set.of(createCartItemDto())).setUserId(ID);
    }
}
