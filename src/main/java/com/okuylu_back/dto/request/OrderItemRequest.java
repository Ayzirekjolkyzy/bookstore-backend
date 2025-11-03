package com.okuylu_back.dto.request;

import jakarta.validation.constraints.NotNull;

public class OrderItemRequest {
    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    public OrderItemRequest() {}


    public @NotNull(message = "Book ID is required") Long getBookId() {
        return bookId;
    }

    public void setBookId(@NotNull(message = "Book ID is required") Long bookId) {
        this.bookId = bookId;
    }

    public @NotNull(message = "Quantity is required") Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "Quantity is required") Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItemRequest(Long bookId, Integer quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
}