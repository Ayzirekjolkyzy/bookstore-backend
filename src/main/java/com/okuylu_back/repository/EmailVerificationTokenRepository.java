package com.okuylu_back.repository;

import com.okuylu_back.model.EmailVerificationToken;
import com.okuylu_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);
    void deleteByUser(User user);
}
