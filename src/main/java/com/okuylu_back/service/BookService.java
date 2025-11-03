package com.okuylu_back.service;

import com.okuylu_back.dto.responses.BookResponse;
import java.util.List;

public interface BookService {

    BookResponse getById(Long id);
    List<BookResponse> findByTitleOrAuthor_Name(String title);
    List<BookResponse> getBooksByGenre(Long genreId);
    List<BookResponse> getBooksByTag(Long tagId);
    List<BookResponse> getAllBooks();
    List<BookResponse> searchSmartBooks(String query);
}
