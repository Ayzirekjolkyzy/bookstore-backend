package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.TagRequest;
import com.okuylu_back.dto.responses.DiscountResponse;
import com.okuylu_back.dto.responses.TagResponse;
import com.okuylu_back.utils.exceptions.ResourceNotFoundException;
import com.okuylu_back.model.Tag;
import com.okuylu_back.repository.TagRepository;
import com.okuylu_back.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public TagResponse createTag(TagRequest tagRequest) {
        if (tagRepository.existsByName(tagRequest.getName())) {
            throw new IllegalArgumentException("A tag with this name already exists: " + tagRequest.getName());
        }
        Tag tag = new Tag();
        tag.setName(tagRequest.getName());

        tagRepository.save(tag);
        return new TagResponse(tag.getTagId(),tag.getName());
    }

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagResponse(tag.getTagId(),
                        tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with ID " + tagId + " not found."));
        return new TagResponse(tag.getTagId(), tag.getName());
    }

    @Override
    public TagResponse updateTag(Long tagId, TagRequest updatedTagRequest) {
        Tag existingTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with ID " + tagId + " not found."));

        existingTag.setName(updatedTagRequest.getName());

        tagRepository.save(existingTag);
        return new TagResponse(existingTag.getTagId(), existingTag.getName());
    }

    @Override
    public void deleteTag(Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new ResourceNotFoundException("Tag with ID " + tagId + " not found.");
        }
        tagRepository.deleteById(tagId);
    }

}