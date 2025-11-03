package com.okuylu_back.service.impl;

import com.okuylu_back.dto.responses.WishlistResponse;
import com.okuylu_back.model.Book;
import com.okuylu_back.model.User;
import com.okuylu_back.model.Wishlist;
import com.okuylu_back.repository.BookRepository;
import com.okuylu_back.repository.UserRepository;
import com.okuylu_back.repository.WishlistRepository;
import com.okuylu_back.service.DiscountService;
import com.okuylu_back.service.WishlistService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final DiscountService discountService;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, UserRepository userRepository, BookRepository bookRepository, DiscountService discountService) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.discountService = discountService;
    }

    @Override
    public void addToWishlist(String email, Long bookId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book with id " + bookId + " not found"));

        if (wishlistRepository.findByUserAndBook(user, book).isPresent()) {
            throw new RuntimeException("Book already in wishlist");
        }

        wishlistRepository.save(new Wishlist(user, book));
    }

    @Override
    public void removeFromWishlist(String email, Long bookId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book with id " + bookId + " not found"));

        wishlistRepository.findByUserAndBook(user, book)
                .ifPresent(wishlistRepository::delete);
    }

    @Override
    public List<WishlistResponse> getUserWishlist(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));



        List<Wishlist> wishlistList = wishlistRepository.findByUser(user);
        return wishlistList.stream()
                .map(wishlist -> {
                    Book book = wishlist.getBook();
                    BigDecimal price = book.getPrice();
                    BigDecimal discountPrice = discountService.calculateDiscountedPrice(book);

                    return new WishlistResponse(
                            wishlist.getWishlistId(),
                            book.getBookId(),
                            price,
                            discountPrice
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void clearWishlist(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
        wishlistRepository.deleteAll(wishlistRepository.findByUser(user));
    }


}
