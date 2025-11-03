package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.PublisherRequest;
import com.okuylu_back.utils.exceptions.ResourceNotFoundException;
import com.okuylu_back.model.Publisher;
import com.okuylu_back.repository.PublisherRepository;
import com.okuylu_back.service.PublisherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public Publisher createPublisher(PublisherRequest publisherRequest) {
        if (publisherRepository.existsByName(publisherRequest.getName())) {
            throw new IllegalArgumentException("Издатель с таким именем уже существует!");
        }

        Publisher publisher = new Publisher();
        publisher.setName(publisherRequest.getName());
        publisher.setAddress(publisherRequest.getAddress());

        return publisherRepository.save(publisher);
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with ID " + id + " not found."));
    }

    @Override
    public Publisher updatePublisher(Long id, PublisherRequest updatedPublisherRequest) {
        Publisher existingPublisher = getPublisherById(id);

        existingPublisher.setName(updatedPublisherRequest.getName());
        existingPublisher.setAddress(updatedPublisherRequest.getAddress());

        return publisherRepository.save(existingPublisher);
    }

    @Override
    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Publisher with ID " + id + " not found.");
        }
        publisherRepository.deleteById(id);
    }
}