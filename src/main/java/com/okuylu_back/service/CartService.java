package com.okuylu_back.service;

import com.okuylu_back.dto.request.CartItemRequest;
import com.okuylu_back.dto.responses.CartItemResponse;

import java.util.List;

public interface CartService {

    void addToCart(String email, CartItemRequest cartItemRequest);
    List<CartItemResponse> getCartItems(String email);
    void removeFromCart(Long cartItemId);
    void clearCart(String email);
    void decreaseQuantity(Long cartItemId);
    void increaseQuantity(Long cartItemId);
}
