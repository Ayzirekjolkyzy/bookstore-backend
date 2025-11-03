package com.okuylu_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDto {

    private Long authorId;

    @NotBlank(message = "Имя автора не может быть пустым")
    @Size(max = 255, message = "Author name length must not exceed 255 characters")
    private String name;

    public AuthorDto() {
    }

    public AuthorDto(Long authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public @NotBlank(message = "Author name must contain at least 1 character") @Size(max = 255, message = "Author name length must not exceed 255 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Author name must contain at least 1 character") @Size(max = 255, message = "Author name length must not exceed 255 characters") String name) {
        this.name = name;
    }
}
