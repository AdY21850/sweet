package com.example.sweet.repository;

import com.example.sweet.model.Order;
import com.example.sweet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ Fetch orders for a user (order history)
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
