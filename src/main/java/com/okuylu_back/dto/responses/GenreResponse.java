package com.okuylu_back.dto.responses;

public class GenreResponse {

    private Long genreId;
    private String name;

    public GenreResponse() {
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GenreResponse(Long genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }
}
