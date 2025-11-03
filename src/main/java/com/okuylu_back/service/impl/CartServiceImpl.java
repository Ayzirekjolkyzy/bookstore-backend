package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.CartItemRequest;
import com.okuylu_back.dto.responses.CartItemResponse;
import com.okuylu_back.model.Book;
import com.okuylu_back.model.CartItem;
import com.okuylu_back.model.User;
import com.okuylu_back.repository.BookRepository;
import com.okuylu_back.repository.CartItemRepository;
import com.okuylu_back.repository.UserRepository;
import com.okuylu_back.service.CartService;
import com.okuylu_back.service.DiscountService;
import com.okuylu_back.utils.exceptions.CartItemNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final DiscountService discountService;


    public CartServiceImpl(CartItemRepository cartItemRepository, BookRepository bookRepository, UserRepository userRepository, DiscountService discountService) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.discountService = discountService;
    }


    @Transactional
    @Override
    public void addToCart(String email, CartItemRequest cartItemRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(cartItemRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStockQuantity() < cartItemRequest.getQuantity()) {
            throw new RuntimeException("Not enough books in stock");
        }

        CartItem existingCartItem = cartItemRepository.findByUserAndBook(user, book);

        if (existingCartItem != null) {
            int updatedQuantity = existingCartItem.getQuantity() + cartItemRequest.getQuantity();
            // Проверяем, не превышает ли новое количество доступное количество на складе
            if (book.getStockQuantity() < updatedQuantity) {
                throw new RuntimeException("Not enough books in stock");
            }
            existingCartItem.setQuantity(updatedQuantity);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setBook(book);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public List<CartItemResponse> getCartItems(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new RuntimeException("User not found"));
        List<CartItem> cartItems = cartItemRepository.findByUser(user);


        return cartItems.stream()
                .map(item -> {
                    Book book = item.getBook();
                    int availableStock = book.getStockQuantity();
                    boolean available=true;
                    if (availableStock == 0)
                        available=false;
                    else if(availableStock<item.getQuantity()) {
                        item.setQuantity(availableStock);
                        cartItemRepository.save(item);
                    }
                    BigDecimal price = book.getPrice().setScale(2, RoundingMode.HALF_UP);
                    BigDecimal discountPrice = discountService.calculateDiscountedPrice(book);
                    BigDecimal totalPrice = discountPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                    return new CartItemResponse(
                            item.getCart_id(),
                            book.getBookId(),
                            item.getQuantity(),
                            price,
                            discountPrice,
                            totalPrice.setScale(2, RoundingMode.HALF_UP),
                            available
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + cartItemId));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(cartItems);
    }

    @Transactional
    @Override
    public void increaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + cartItemId));

        if (cartItem.getBook().getStockQuantity() <= cartItem.getQuantity()) {
            throw new RuntimeException("Not enough books in stock");
        }

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public void decreaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + cartItemId));

        int newQuantity = cartItem.getQuantity() - 1;
        if (newQuantity > 0) {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }
    }
}