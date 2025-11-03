package com.okuylu_back.service;

import com.okuylu_back.dto.request.PublisherRequest;
import com.okuylu_back.model.Publisher;

import java.util.List;

public interface PublisherService {
    Publisher createPublisher(PublisherRequest publisherRequest);
    List<Publisher> getAllPublishers();
    Publisher getPublisherById(Long id);
    Publisher updatePublisher(Long id, PublisherRequest updatedPublisher);
    void deletePublisher(Long id);
}
