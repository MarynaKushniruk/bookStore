package com.example.bookstore.repository;

import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryIntegrationTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by valid ID")
    @Sql(scripts = "classpath:database/add-for-shopping_cart-cart_item-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-for-shopping_cart-cart_item-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ShoppingCartExists_Ok() {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(1L);
        Assertions.assertNotNull(shoppingCart);
        Assertions.assertEquals(1L, shoppingCart.get().getUser().getId());
    }

    @Test
    @DisplayName("Find shopping cart by not existing ID")
    @Sql(scripts = "classpath:database/add-for-shopping_cart-cart_item-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-for-shopping_cart-cart_item-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ShoppingCarNotExists_ReturnIsEmpty() {
        long id = 5;
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(id);
        Assertions.assertTrue(shoppingCart.isEmpty());
    }
}
