package com.example.sweet.controller;

import com.example.sweet.model.Order;
import com.example.sweet.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ==========================
    // USER: PLACE ORDER (CHECKOUT)
    // ==========================
    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public Order placeOrder(Authentication authentication) {
        String email = authentication.getName();
        return orderService.placeOrder(email);
    }

    // ==========================
    // USER: VIEW ORDER HISTORY
    // ==========================
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        return orderService.getOrdersForUser(email);
    }

    // ==========================
    // ADMIN: VIEW ALL ORDERS
    // ==========================
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
