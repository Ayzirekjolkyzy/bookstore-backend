package com.okuylu_back.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public class BookRequest {

    @NotBlank(message = "Название книги не может быть пустым")
    private String title;

    private Long authorId;

    private Long publisherId;
    private Long discountId;

    @NotBlank(message = "Описание книги не может быть пустым")
    private String description;

    private String imageUrl;

    @NotNull(message = "Цена книги обязательна")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private BigDecimal price;

    @NotNull(message = "Количество на складе обязательно")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private Integer stockQuantity;

    private Set<Long> genreIds;
    private Set<Long> tagIds;

    public @NotBlank(message = "Название книги не может быть пустым") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Название книги не может быть пустым") String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public @NotBlank(message = "Описание книги не может быть пустым") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Описание книги не может быть пустым") String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @NotNull(message = "Цена книги обязательна") @Min(value = 0, message = "Цена не может быть отрицательной") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "Цена книги обязательна") @Min(value = 0, message = "Цена не может быть отрицательной") BigDecimal price) {
        this.price = price;
    }

    public @NotNull(message = "Количество на складе обязательно") @Min(value = 0, message = "Количество не может быть отрицательным") Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(@NotNull(message = "Количество на складе обязательно") @Min(value = 0, message = "Количество не может быть отрицательным") Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Set<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(Set<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public BookRequest() {
    }

    public BookRequest(String title, Long authorId, Long publisherId, Long discountId, String description, String imageUrl, BigDecimal price, Integer stockQuantity, Set<Long> genreIds, Set<Long> tagIds) {
        this.title = title;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.discountId = discountId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.genreIds = genreIds;
        this.tagIds = tagIds;
    }
}
