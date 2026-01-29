package com.example.sweet.repository;

import com.example.sweet.model.Cart;
import com.example.sweet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // ✅ Fetch cart for a specific user
    Optional<Cart> findByUser(User user);
}
