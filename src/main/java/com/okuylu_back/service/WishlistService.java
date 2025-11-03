package com.okuylu_back.service;

import com.okuylu_back.dto.responses.WishlistResponse;
import java.util.List;

public interface WishlistService {
    void addToWishlist(String email, Long bookId);
    void removeFromWishlist(String email, Long bookId);
    List<WishlistResponse> getUserWishlist(String email);
    void clearWishlist(String email);

}
