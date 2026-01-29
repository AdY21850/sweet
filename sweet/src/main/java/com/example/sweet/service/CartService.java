package com.example.sweet.service;

import com.example.sweet.exception.ResourceNotFoundException;
import com.example.sweet.model.Cart;
import com.example.sweet.model.CartItem;
import com.example.sweet.model.Sweet;
import com.example.sweet.model.User;
import com.example.sweet.repository.CartItemRepository;
import com.example.sweet.repository.CartRepository;
import com.example.sweet.repository.SweetRepository;
import com.example.sweet.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final SweetRepository sweetRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            SweetRepository sweetRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.sweetRepository = sweetRepository;
        this.userRepository = userRepository;
    }

    // ==========================
    // GET OR CREATE CART
    // ==========================
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    // ==========================
    // GET CART FOR USER
    // ==========================
    public Cart getUserCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return getOrCreateCart(user);
    }

    // ==========================
    // ADD TO CART
    // ==========================
    public Cart addToCart(String email, Long sweetId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Sweet not found"));

        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findByCartAndSweet(cart, sweet)
                .orElse(null);

        if (item == null) {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setSweet(sweet);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        } else {
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        }

        return cart;
    }

    // ==========================
    // UPDATE QUANTITY
    // ==========================
    public Cart updateQuantity(String email, Long itemId, int quantity) {

        if (quantity <= 0) {
            return removeItem(email, itemId);
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return getUserCart(email);
    }

    // ==========================
    // REMOVE ITEM
    // ==========================
    public Cart removeItem(String email, Long itemId) {

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItemRepository.delete(item);

        return getUserCart(email);
    }

    // ==========================
    // CLEAR CART
    // ==========================
    public void clearCart(String email) {
        Cart cart = getUserCart(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
