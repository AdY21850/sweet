package com.example.sweet.mapper;

import com.example.sweet.dto.CartItemResponse;
import com.example.sweet.dto.CartResponse;
import com.example.sweet.dto.SweetResponse;
import com.example.sweet.model.Cart;
import com.example.sweet.model.CartItem;
import com.example.sweet.model.Sweet;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponse toResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getItems()
                        .stream()
                        .map(CartMapper::toItemResponse)
                        .collect(Collectors.toList())
        );
    }

    private static CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getQuantity(),
                toSweetResponse(item.getSweet())
        );
    }

    private static SweetResponse toSweetResponse(Sweet sweet) {
        return new SweetResponse(
                sweet.getId(),
                sweet.getName(),
                sweet.getPrice(),
                sweet.getImageUrl()
        );
    }
}
