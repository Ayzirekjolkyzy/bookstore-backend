package com.okuylu_back.model;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
    @Table(name = "order_items")
    public class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long orderItemId;

        @ManyToOne
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;

        @ManyToOne
        @JoinColumn(name = "book_id", nullable = false)
        private Book book;

        @Column(nullable = false)
        private int quantity;

        @Column(nullable = false)
        private BigDecimal price;

    public OrderItem() {
    }

    public OrderItem(Long orderItemId, Order order, Book book, int quantity, BigDecimal price) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
}
