
package com.okuylu_back.controller.user;

import com.okuylu_back.dto.responses.WishlistResponse;
import com.okuylu_back.service.WishlistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/wishlist")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "wishlist-controller")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<String> addToWishlist(Authentication authentication, @PathVariable Long bookId) {
        String email = authentication.getName(); // Получаем email пользователя из SecurityContext
        wishlistService.addToWishlist(email, bookId);
        return ResponseEntity.ok("Book added to wishlist");
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> removeFromWishlist(Authentication authentication, @PathVariable Long bookId) {
        String email = authentication.getName(); // Получаем email пользователя из SecurityContext
        wishlistService.removeFromWishlist(email, bookId);
        return ResponseEntity.ok("Book removed from wishlist");
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getUserWishlist(Authentication authentication) {
        String email = authentication.getName(); // Получаем email пользователя из SecurityContext
        return ResponseEntity.ok(wishlistService.getUserWishlist(email));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearWishlist(Authentication authentication) {
        String email = authentication.getName();
        wishlistService.clearWishlist(email);
        return ResponseEntity.ok("Wishlist cleared");
    }


}