package com.okuylu_back.dto.request;

public class CartItemRequest {

    private Long cartItemId;   // Для ответа
    private Long bookId;
    private int quantity;

    public CartItemRequest() {
    }

    public CartItemRequest(Long cartItemId, Long bookId, int quantity) {
        this.cartItemId = cartItemId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}