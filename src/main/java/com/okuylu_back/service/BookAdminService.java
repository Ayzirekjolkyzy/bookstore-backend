package com.okuylu_back.service;

import com.okuylu_back.dto.request.BookRequest;
import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.model.Book;

public interface BookAdminService {
    Book createBook(BookRequest dto);
    Book updateBook(Long id, BookRequest dto);
    void deleteBook(Long id);
    Book findById(Long id);
    PageResponse<Book> getAllBooks(int page, int size);
    Book addGenreToBook(Long bookId, Long genreId);
    Book addTagToBook(Long bookId, Long tagId);
    Book removeGenreFromBook(Long bookId, Long genreId);
    Book removeTagFromBook(Long bookId, Long tagId);
}