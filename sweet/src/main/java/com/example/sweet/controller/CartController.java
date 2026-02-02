package com.example.sweet.controller;

import com.example.sweet.dto.CartResponse;
import com.example.sweet.mapper.CartMapper;
import com.example.sweet.model.Cart;
import com.example.sweet.service.CartService;
import org.springframework.http.ResponseEntity;
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
    public CartResponse getCart(Authentication authentication) {
        Cart cart = cartService.getUserCart(authentication.getName());
        return CartMapper.toResponse(cart);
    }

    // ✅ Add sweet to cart (NOBODY RETURNED)
    @PostMapping("/add/{sweetId}")
    public ResponseEntity<Void> addToCart(
            Authentication authentication,
            @PathVariable Long sweetId
    ) {
        cartService.addToCart(authentication.getName(), sweetId);
        return ResponseEntity.ok().build();
    }

    // ✅ Update item quantity
    @PutMapping("/update/{itemId}")
    public CartResponse updateQuantity(
            Authentication authentication,
            @PathVariable Long itemId,
            @RequestParam int quantity
    ) {
        Cart cart = cartService.updateQuantity(
                authentication.getName(),
                itemId,
                quantity
        );
        return CartMapper.toResponse(cart);
    }

    // ✅ Remove item from cart
    @DeleteMapping("/remove/{itemId}")
    public CartResponse removeItem(
            Authentication authentication,
            @PathVariable Long itemId
    ) {
        Cart cart = cartService.removeItem(
                authentication.getName(),
                itemId
        );
        return CartMapper.toResponse(cart);
    }

    // ✅ Clear entire cart
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.ok().build();
    }
}
