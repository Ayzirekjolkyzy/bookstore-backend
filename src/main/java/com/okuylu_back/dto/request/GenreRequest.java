package com.okuylu_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenreRequest {

    private Long genre_id;

    @NotBlank(message = "Название жанра не может быть пустым")
    @Size(max = 255, message = "Genre name length must not exceed 255 characters")
    private String name;

    public GenreRequest() {
    }

    public GenreRequest(Long genre_id, String name) {
        this.genre_id = genre_id;
        this.name = name;
    }

    public Long getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Long genre_id) {
        this.genre_id = genre_id;
    }

    public @NotBlank(message = "Название жанра не может быть пустым") @Size(max = 255, message = "Genre name length must not exceed 255 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Название жанра не может быть пустым") @Size(max = 255, message = "Genre name length must not exceed 255 characters") String name) {
        this.name = name;
    }


}
