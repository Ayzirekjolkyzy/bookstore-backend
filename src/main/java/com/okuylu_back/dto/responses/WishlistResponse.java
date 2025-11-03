package com.okuylu_back.dto.responses;


import java.math.BigDecimal;

public class WishlistResponse {

    private Long wishlistId;
    private Long bookId;
    private BigDecimal price;          // Обычная цена
    private BigDecimal discountPrice;


    public WishlistResponse(){

    }

    public WishlistResponse(Long wishlistId, Long bookId, BigDecimal price, BigDecimal discountPrice) {
        this.wishlistId = wishlistId;
        this.bookId = bookId;
        this.price = price;
        this.discountPrice = discountPrice;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
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
}
