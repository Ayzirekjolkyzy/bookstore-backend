package com.okuylu_back.controller;

import com.okuylu_back.dto.responses.BookResponse;
import com.okuylu_back.dto.responses.DiscountResponse;
import com.okuylu_back.dto.responses.GenreResponse;
import com.okuylu_back.dto.responses.TagResponse;
import com.okuylu_back.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/public/books")
@Tag(name = "Book Controller")

public class BookController {

    private final BookService bookService;
    private final GenreService genreService;
    private final TagService tagService;
    private final DiscountService discountService;
    private static final Logger log = LoggerFactory.getLogger(BookController.class);




    public BookController(BookService bookService, GenreService genreService, TagService tagService, DiscountService discountService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.tagService = tagService;
        this.discountService = discountService;
    }


    @Operation(summary = "Получить книгу по её ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.getById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Получить список всех книг")
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }


    @Operation(summary = "Найти книги по части названия или по автору (игнорируя регистр)")
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooksByTitle(@RequestParam String title) {
        List<BookResponse> books = bookService.findByTitleOrAuthor_Name(title);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Умный поиск книг по описанию (AI)")
    @GetMapping("/smart-search")
    public ResponseEntity<List<BookResponse>> smartSearch(@RequestParam String query) {
        List<BookResponse> books = bookService.searchSmartBooks(query);
        log.debug("Smart search result: {}", books);
        return ResponseEntity.ok(books);
    }


    @Operation(summary = "Получить список книг по жанру")
    @GetMapping("/genres/{genreId}")
    public ResponseEntity<List<BookResponse>> getBooksByGenre(@PathVariable Long genreId) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genreId));
    }

    @Operation(summary = "Получить список книг по тегу")
    @GetMapping("/tags/{tagId}")
    public ResponseEntity<List<BookResponse>> getBooksByTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(bookService.getBooksByTag(tagId));
    }

    @Operation(summary = "Получить все жанры")
    @GetMapping("/genres")
    public ResponseEntity<List<GenreResponse>> getAllGenres() {
        List<GenreResponse> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @Operation(summary = "Получить все теги")
    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Получить баннер скидок")
    @GetMapping("/discounts")
    public ResponseEntity<List<DiscountResponse>> getDiscountBanner() {
        return ResponseEntity.ok(discountService.getActiveDiscounts());
    }

}
