package com.okuylu_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PublisherRequest {

    private Long publisherId;

    @NotBlank(message = "Название издательства не может быть пустым")
    @Size(max = 255, message = "Название издательства не должно превышать 255 символов")
    private String name;

    @Size(max = 255, message = "Адрес издательства не должен превышать 255 символов")
    private String address;

    public PublisherRequest() {
    }

    public PublisherRequest(Long publisherId, String name, String address) {
        this.publisherId = publisherId;
        this.name = name;
        this.address = address;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}