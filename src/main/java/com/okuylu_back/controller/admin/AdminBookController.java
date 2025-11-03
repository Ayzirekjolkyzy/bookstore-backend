package com.okuylu_back.controller.admin;

import com.okuylu_back.dto.request.BookRequest;
import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.model.Book;
import com.okuylu_back.service.BookAdminService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {

    private final BookAdminService bookAdminService;

    public AdminBookController(BookAdminService bookAdminService) {
        this.bookAdminService = bookAdminService;
    }

    @Operation(summary = "Добавить новую книгу (доступно только администратору)")
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody BookRequest dto) {
        System.out.println(dto);
        return ResponseEntity.ok(bookAdminService.createBook(dto));
    }

    @Operation(summary = "Редактировать информацию о книге (доступно только администратору)")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest dto) {
        return ResponseEntity.ok(bookAdminService.updateBook(id, dto));
    }

    @Operation(summary = "Удалить книгу (доступно только администратору)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookAdminService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "Получить список всех книг с пагинацией")
    @GetMapping
    public ResponseEntity<PageResponse<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<Book> books = bookAdminService.getAllBooks(page, size);
        return ResponseEntity.ok(books);
    }


    @Operation(summary = "Получить книгу по её ID(доступно только администратору)")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookAdminService.findById(id);
        return ResponseEntity.ok(book);
    }



    @Operation(summary = "Связать книгу с жанром(доступно только администратору)")
    @PostMapping("/{bookId}/genre/{genreId}")
    public ResponseEntity<Book> addGenreToBook(@PathVariable Long bookId, @PathVariable Long genreId) {
        Book updatedBook = bookAdminService.addGenreToBook(bookId, genreId);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Связать книгу с тегом(доступно только администратору)")
    @PostMapping("/{bookId}/tag/{tagId}")
    public ResponseEntity<Book> addTagToBook(@PathVariable Long bookId, @PathVariable Long tagId) {
        Book updatedBook = bookAdminService.addTagToBook(bookId, tagId);
        return ResponseEntity.ok(updatedBook);
    }
    @Operation(summary = "Отвязать жанр от книги(доступно только администратору)")
    @DeleteMapping("/{bookId}/genre/{genreId}")
    public ResponseEntity<Book> removeGenreFromBook(@PathVariable Long bookId, @PathVariable Long genreId) {
        Book updatedBook = bookAdminService.removeGenreFromBook(bookId, genreId);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Отвязать тег от книги(доступно только администратору)")
    @DeleteMapping("/{bookId}/tag/{tagId}")
    public ResponseEntity<Book> removeTagFromBook(@PathVariable Long bookId, @PathVariable Long tagId) {
        Book updatedBook = bookAdminService.removeTagFromBook(bookId, tagId);
        return ResponseEntity.ok(updatedBook);
    }


}