package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.BookRequest;
import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.utils.exceptions.ResourceNotFoundException;
import com.okuylu_back.model.*;
import com.okuylu_back.repository.BookRepository;
import com.okuylu_back.repository.GenreRepository;
import com.okuylu_back.repository.TagRepository;
import com.okuylu_back.service.AuthorService;
import com.okuylu_back.service.BookAdminService;
import com.okuylu_back.service.DiscountService;
import com.okuylu_back.service.PublisherService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class BookAdminServiceImpl implements BookAdminService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final DiscountService discountService;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;


    public BookAdminServiceImpl(BookRepository bookRepository, AuthorService authorService,
                                PublisherService publisherService, DiscountService discountService, GenreRepository genreRepository, TagRepository tagRepository) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.discountService = discountService;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Book createBook(BookRequest dto) {

        Author author = dto.getAuthorId() != null
                ? authorService.getAuthorById(dto.getAuthorId())
                : null;
        Publisher publisher = dto.getPublisherId() != null
                ? publisherService.getPublisherById(dto.getPublisherId())
                : null;
        Discount discount = dto.getDiscountId() != null
                ? discountService.getDiscountByIdAsDiscount(dto.getDiscountId())
                : null;

        Book newBook = new Book();
        newBook.setTitle(dto.getTitle());
        newBook.setAuthor(author);
        newBook.setDiscount(discount);
        newBook.setPublisher(publisher);
        newBook.setDescription(dto.getDescription());
        newBook.setImageUrl(dto.getImageUrl());
        newBook.setPrice(dto.getPrice());
        newBook.setStockQuantity(dto.getStockQuantity());

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.getGenreIds()));
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));

        newBook.setGenres(genres);
        newBook.setTags(tags);

        return bookRepository.save(newBook);
    }



    @Override
    @Transactional
    public Book updateBook(Long id, BookRequest dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));

        if (dto.getTitle() != null) {
            existingBook.setTitle(dto.getTitle());
        }

        if (dto.getAuthorId() != null) {
            Author author = authorService.getAuthorById(dto.getAuthorId());
            existingBook.setAuthor(author);
        }

        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherService.getPublisherById(dto.getPublisherId());
            existingBook.setPublisher(publisher);
        }

        if (dto.getDiscountId() != null) {
            Discount discount = discountService.getDiscountByIdAsDiscount(dto.getDiscountId());
            existingBook.setDiscount(discount);
        }

        if (dto.getDescription() != null) {
            existingBook.setDescription(dto.getDescription());
        }

        if (dto.getImageUrl() != null) {
            existingBook.setImageUrl(dto.getImageUrl());
        }

        if (dto.getPrice() != null) {
            existingBook.setPrice(dto.getPrice());
        }

        if (dto.getStockQuantity() != null) {
            existingBook.setStockQuantity(dto.getStockQuantity());
        }

        existingBook.setUpdatedAt(LocalDateTime.now());

        if (dto.getGenreIds() != null) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.getGenreIds()));
            existingBook.setGenres(genres);
        }

        if (dto.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
            existingBook.setTags(tags);
        }

        return bookRepository.save(existingBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));
        bookRepository.delete(book);
    }



    @Override
    @Transactional
    public PageResponse<Book> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return PageResponse.fromPage(booksPage);
    }


    @Override
    @Transactional
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));
    }



    @Override
    @Transactional
    public Book addGenreToBook(Long bookId, Long genreId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        book.getGenres().add(genre);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book addTagToBook(Long bookId, Long tagId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        book.getTags().add(tag);
        return bookRepository.save(book);
    }



    @Override
    @Transactional
    public Book removeGenreFromBook(Long bookId, Long genreId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        book.getGenres().remove(genre);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book removeTagFromBook(Long bookId, Long tagId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        book.getTags().remove(tag);
        return bookRepository.save(book);
    }
}
