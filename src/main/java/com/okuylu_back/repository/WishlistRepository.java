package com.okuylu_back.repository;

import com.okuylu_back.model.Book;
import com.okuylu_back.model.User;
import com.okuylu_back.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findByUser(User user);
    Optional<Wishlist>findByUserAndBook(User user, Book book);
    void deleteByUserAndBook(User user, Book book);
}
