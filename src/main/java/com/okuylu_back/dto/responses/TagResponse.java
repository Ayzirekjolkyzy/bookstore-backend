package com.okuylu_back.dto.responses;

public class TagResponse {
    private Long tagId;
    private String name;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagResponse(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public TagResponse() {
    }
}
