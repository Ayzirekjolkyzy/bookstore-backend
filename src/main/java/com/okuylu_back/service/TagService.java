package com.okuylu_back.service;

import com.okuylu_back.dto.request.TagRequest;
import com.okuylu_back.dto.responses.TagResponse;
import java.util.List;

public interface TagService {
    List<TagResponse> getAllTags();
    TagResponse getTagById(Long id);
    TagResponse createTag(TagRequest tag);
    TagResponse updateTag(Long id, TagRequest tagDetails);
    void deleteTag(Long id);
}