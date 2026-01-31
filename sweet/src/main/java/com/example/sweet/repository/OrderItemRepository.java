package com.example.sweet.repository;

import com.example.sweet.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // ✅ NEW: Finds Sweet IDs ordered most frequently (by total quantity)
    @Query("""
        SELECT oi.sweet.id
        FROM OrderItem oi
        GROUP BY oi.sweet.id
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<Long> findPopularSweetIds(Pageable pageable);
}
