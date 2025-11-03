package com.okuylu_back.repository;
import com.okuylu_back.model.Book;
import com.okuylu_back.model.CartItem;
import com.okuylu_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    CartItem findByUserAndBook(User user, Book book);
}
