package com.okuylu_back.service.impl;

import com.okuylu_back.dto.responses.AiBookDto;
import com.okuylu_back.dto.responses.BookResponse;
import com.okuylu_back.model.Book;
import com.okuylu_back.repository.BookRepository;
import com.okuylu_back.service.BookService;
import com.okuylu_back.service.DiscountService;
import com.okuylu_back.utils.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final DiscountService discountService;

    public BookServiceImpl(BookRepository bookRepository, DiscountService discountService) {
        this.bookRepository = bookRepository;
        this.discountService = discountService;
    }

    @Transactional
    @Override
    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));
        BigDecimal discountPrice = discountService.calculateDiscountedPrice(book);
        return new BookResponse(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getDiscount(),
                book.getDescription(),
                book.getImageUrl(),
                book.getPrice(),
                discountPrice,
                book.getStockQuantity(),
                book.getGenres(),
                book.getTags()
        );
    }

    @Transactional
    @Override
    public List<BookResponse> findByTitleOrAuthor_Name(String query) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(query, query);
        return books.stream()
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getDiscount(),
                        book.getDescription(),
                        book.getImageUrl(),
                        book.getPrice(),
                        discountService.calculateDiscountedPrice(book),
                        book.getStockQuantity(),
                        book.getGenres(),
                        book.getTags()
                ))
                .collect(Collectors.toSet()) // Убираем дубли
                .stream()
                .toList();
    }

    @Transactional
    @Override
    public List<BookResponse> getBooksByGenre(Long genreId) {
        List<Book> books = bookRepository.findByGenres_genreId(genreId);

        return books.stream()
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getDiscount(),
                        book.getDescription(),
                        book.getImageUrl(),
                        book.getPrice(),
                        discountService.calculateDiscountedPrice(book),
                        book.getStockQuantity(),
                        book.getGenres(),
                        book.getTags()
                ))
                .collect(Collectors.toList());    }

    @Transactional
    @Override
    public List<BookResponse> getBooksByTag(Long tagId) {
        List<Book> books = bookRepository.findByTags_tagId(tagId);

        return books.stream()
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getDiscount(),
                        book.getDescription(),
                        book.getImageUrl(),
                        book.getPrice(),
                        discountService.calculateDiscountedPrice(book),
                        book.getStockQuantity(),
                        book.getGenres(),
                        book.getTags()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getDiscount(),
                        book.getDescription(),
                        book.getImageUrl(),
                        book.getPrice(),
                        discountService.calculateDiscountedPrice(book),
                        book.getStockQuantity(),
                        book.getGenres(),
                        book.getTags()
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public List<BookResponse> searchSmartBooks(String query) {
        String aiApiUrl = "https://oku-kg-ai.onrender.com/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("query", query);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<AiBookDto[]> response = restTemplate.postForEntity(
                aiApiUrl,
                request,
                AiBookDto[].class
        );

        AiBookDto[] aiBooks = response.getBody();
        if (aiBooks == null || aiBooks.length == 0) {
            return List.of(); // ничего не найдено
        }

        List<Long> bookIds = Arrays.stream(aiBooks)
                .map(AiBookDto::getId)
                .filter(Objects::nonNull)
                .toList();

        List<Book> books = bookRepository.findAllById(bookIds);

        return books.stream()
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getDiscount(),
                        book.getDescription(),
                        book.getImageUrl(),
                        book.getPrice(),
                        discountService.calculateDiscountedPrice(book),
                        book.getStockQuantity(),
                        book.getGenres(),
                        book.getTags()
                ))
                .toList();
    }

}
