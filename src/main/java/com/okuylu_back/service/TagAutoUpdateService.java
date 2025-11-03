package com.okuylu_back.service;

import com.okuylu_back.model.Book;
import com.okuylu_back.model.Tag;
import com.okuylu_back.repository.BookRepository;
import com.okuylu_back.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagAutoUpdateService {

    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final BookService bookService;
    private final BookAdminService bookAdminService;

    public TagAutoUpdateService(BookRepository bookRepository, TagRepository tagRepository, BookService bookService, BookAdminService bookAdminService) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.bookService = bookService;
        this.bookAdminService = bookAdminService;
    }


    @Scheduled(cron = "0 0 0 1 * ?") // Запуск в 00:00 1-го числа каждого месяца
    @Transactional
    public void updatePopularBooksTag() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);

        // 1. Получаем топ-N книг за последний месяц
        List<Book> topBooks = bookRepository.findTopBooksByOrdersBetween(
                startDate,
                endDate,
                PageRequest.of(0, 15)
        );

        Tag popularTag = tagRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Tag with ID=2 not found"));
        Long popularTagId = 2L;

        List<Book> currentBooks = bookRepository.findByTags_tagId(popularTagId);

        int booksToUpdate = Math.min(topBooks.size(), 15);

        if (!currentBooks.isEmpty() && booksToUpdate > 0) {
            List<Book> booksToRemove = currentBooks.subList(0, Math.min(booksToUpdate, currentBooks.size()));
            booksToRemove.forEach(book -> bookAdminService.removeTagFromBook(book.getBookId(), 2L));
        }

        if (!topBooks.isEmpty()) {
            Set<Book> currentlyTaggedBooks = new HashSet<>(
                    bookRepository.findByTags_tagId(popularTagId));
            topBooks.stream()
                    .limit(booksToUpdate)
                    .filter(book -> !currentlyTaggedBooks.contains(book))
                    .forEach(book -> bookAdminService.addTagToBook(book.getBookId(), 2L));
        }
    }
}
