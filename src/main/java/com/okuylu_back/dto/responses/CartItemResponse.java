package com.okuylu_back.dto.responses;

import java.math.BigDecimal;

public class CartItemResponse {
    private Long cartItemId;
    private Long bookId;
    private int quantity;
    private BigDecimal price;          // Обычная цена
    private BigDecimal discountPrice;  // Цена со скидкой (если есть)
    private BigDecimal totalPrice;
    private Boolean available;

    public CartItemResponse() {
    }

    public CartItemResponse(Long cartItemId, Long bookId, int quantity, BigDecimal price, BigDecimal discountPrice, BigDecimal totalPrice, Boolean available) {
        this.cartItemId = cartItemId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
        this.available = available;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
