package com.okuylu_back.repository;

import com.okuylu_back.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Boolean existsByName(String name);

    Optional<Tag> findByName(String name);
}