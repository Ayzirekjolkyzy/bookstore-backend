package com.okuylu_back.controller.user;

import com.okuylu_back.dto.request.CartItemRequest;
import com.okuylu_back.dto.responses.CartItemResponse;
import com.okuylu_back.service.CartService;
import com.okuylu_back.utils.exceptions.CartItemNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/cart")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Cart Controller")
public class CartController {

    private final CartService cartService;


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Добавить книгу в корзину")
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(Authentication authentication, @RequestBody CartItemRequest cartItemRequest) {
        String email = authentication.getName(); // email из SecurityContext
        cartService.addToCart(email, cartItemRequest);
        return ResponseEntity.ok("Book added to cart");
    }

    @Operation(summary = "Получить список книг в корзине")
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(cartService.getCartItems(email));
    }

    @Operation(summary = "Очистить корзину")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {
        String email = authentication.getName();
        cartService.clearCart(email);
        return ResponseEntity.ok("Cart cleared");
    }


    @Operation(summary = "Удалить книгу из корзины")
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartItemId) {
        try {
            cartService.removeFromCart(cartItemId);
            return ResponseEntity.ok("Book removed from cart");
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Увеличить количество книги в корзине на 1")
    @PostMapping("/increase/{cartItemId}")
    public ResponseEntity<String> increaseQuantity(@PathVariable Long cartItemId) {
        try {
            cartService.increaseQuantity(cartItemId);
            return ResponseEntity.ok("Quantity increased by 1");
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Уменьшить количество книги в корзине на 1")
    @PostMapping("/decrease/{cartItemId}")
    public ResponseEntity<String> decreaseQuantity(@PathVariable Long cartItemId) {
        try {
            cartService.decreaseQuantity(cartItemId);
            return ResponseEntity.ok("Quantity decreased by 1");
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}