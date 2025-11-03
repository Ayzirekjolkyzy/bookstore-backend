package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.AuthorDto;
import com.okuylu_back.utils.exceptions.ResourceNotFoundException;
import com.okuylu_back.model.Author;
import com.okuylu_back.repository.AuthorRepository;
import com.okuylu_back.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(AuthorDto authorDto) {
        if (authorRepository.existsByName(authorDto.getName())) {
            throw new RuntimeException("Автор с таким именем уже существует: " + authorDto.getName());
        }

        Author author = new Author();
        author.setName(authorDto.getName());

        return authorRepository.save(author);
    }

    @Override
    public Author getAuthorById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id: " + id + " not found"));
    }


    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author updateAuthor(Long id, AuthorDto updatedAuthorDto) {
        Author existingAuthor = getAuthorById(id);
        existingAuthor.setName(updatedAuthorDto.getName());

        return authorRepository.save(existingAuthor);
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);

        authorRepository.delete(author);
    }
}