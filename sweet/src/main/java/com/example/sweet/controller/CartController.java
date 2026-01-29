package com.example.sweet.controller;

import com.example.sweet.model.Cart;
import com.example.sweet.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ✅ Get current user's cart
    @GetMapping
    public Cart getCart(Authentication authentication) {
        String email = authentication.getName();
        return cartService.getUserCart(email);
    }

    // ✅ Add sweet to cart
    @PostMapping("/add/{sweetId}")
    public Cart addToCart(
            Authentication authentication,
            @PathVariable Long sweetId
    ) {
        String email = authentication.getName();
        return cartService.addToCart(email, sweetId);
    }

    // ✅ Update item quantity
    @PutMapping("/update/{itemId}")
    public Cart updateQuantity(
            Authentication authentication,
            @PathVariable Long itemId,
            @RequestParam int quantity
    ) {
        String email = authentication.getName();
        return cartService.updateQuantity(email, itemId, quantity);
    }

    // ✅ Remove item from cart
    @DeleteMapping("/remove/{itemId}")
    public Cart removeItem(
            Authentication authentication,
            @PathVariable Long itemId
    ) {
        String email = authentication.getName();
        return cartService.removeItem(email, itemId);
    }

    // ✅ Clear entire cart
    @DeleteMapping("/clear")
    public void clearCart(Authentication authentication) {
        String email = authentication.getName();
        cartService.clearCart(email);
    }
}
