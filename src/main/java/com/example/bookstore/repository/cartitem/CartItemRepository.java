package com.example.bookstore.repository.cartitem;

import com.example.bookstore.model.CartItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.shoppingCart.id =:shoppingCartId")
    Set<CartItem> findCartItemsByShoppingCartId(@Param("shoppingCartId") Long shoppingCartId);
}
