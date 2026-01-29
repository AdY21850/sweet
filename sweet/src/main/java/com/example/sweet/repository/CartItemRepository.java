package com.example.sweet.repository;

import com.example.sweet.model.Cart;
import com.example.sweet.model.CartItem;
import com.example.sweet.model.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ✅ Find item by cart + sweet (avoid duplicates)
    Optional<CartItem> findByCartAndSweet(Cart cart, Sweet sweet);
}
