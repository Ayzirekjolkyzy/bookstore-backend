package com.okuylu_back.model;

import jakarta.persistence.*;

@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Wishlist() {
    }

    public Wishlist(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    public Wishlist(Long wishlistId, User user, Book book) {
        this.wishlistId = wishlistId;
        this.user = user;
        this.book = book;
    }

    // Геттеры и сеттеры
    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlistId=" + wishlistId +
                ", userId=" + (user != null ? user.getUserId() : null) +
                ", bookId=" + (book != null ? book.getBookId() : null) +
                '}';
    }
}