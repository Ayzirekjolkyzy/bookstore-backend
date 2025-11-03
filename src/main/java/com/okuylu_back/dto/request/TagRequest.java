package com.okuylu_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagRequest {

    private Long tag_id;

    @NotBlank(message = "Название тег не может быть пустым")
    @Size(max = 255, message = "Tag name length must not exceed 255 characters")
    private String name;

    public TagRequest() {
    }

    public TagRequest(Long tag_id, String name) {
        this.tag_id = tag_id;
        this.name = name;
    }

    public Long getTag_id() {
        return tag_id;
    }

    public void setTag_id(Long tag_id) {
        this.tag_id = tag_id;
    }

    public @NotBlank(message = "Название тег не может быть пустым") @Size(max = 255, message = "Tag name length must not exceed 255 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Название тег не может быть пустым") @Size(max = 255, message = "Tag name length must not exceed 255 characters") String name) {
        this.name = name;
    }
}
