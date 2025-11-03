package com.okuylu_back.repository;

import com.okuylu_back.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByGenres_genreId(Long genreId);
    List<Book> findByTags_tagId(Long tagId);
    List<Book> findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(String title, String authorName);

    long countByStockQuantity(int quantity);



    @Query("SELECT b FROM Book b JOIN OrderItem oi ON oi.book = b JOIN Order o ON oi.order = o " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY b " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Book> findTopBooksByOrdersBetween(@Param("startDate") LocalDateTime start,
                                           @Param("endDate") LocalDateTime end,
                                           Pageable pageable);


    @Query("SELECT SUM(b.stockQuantity) FROM Book b")
    Long getTotalStockQuantity();


}
