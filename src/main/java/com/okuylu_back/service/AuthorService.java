package com.okuylu_back.service;

import com.okuylu_back.dto.request.AuthorDto;
import com.okuylu_back.model.Author;

import java.util.List;

public interface AuthorService {
    Author createAuthor(AuthorDto authorDto);
    Author getAuthorById(Long id);
    List<Author> getAllAuthors();
    Author updateAuthor(Long id, AuthorDto authorDto);
    void deleteAuthor(Long id);
}
