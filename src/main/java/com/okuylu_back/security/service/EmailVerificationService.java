package com.okuylu_back.security.service;

import com.okuylu_back.model.EmailVerificationToken;
import com.okuylu_back.model.User;
import com.okuylu_back.repository.EmailVerificationTokenRepository;
import com.okuylu_back.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public EmailVerificationService(EmailVerificationTokenRepository tokenRepository,
                                    UserRepository userRepository,
                                    EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void createVerificationToken(User user) {
        tokenRepository.deleteByUser(user); // Удаляем старый токен
        EmailVerificationToken token = new EmailVerificationToken(user);
        tokenRepository.save(token);

        String verificationLink = "https://oku-kg.onrender.com/api/auth/verify?token=" + token.getToken();
        emailService.sendEmail(user.getEmail(), "Электрондук почта ырастоо",
                "Электрондук почтаңызды ырастоо үчүн шилтемени басыңыз: " + verificationLink);
    }

    @Transactional
    public boolean verifyToken(String token) {
        Optional<EmailVerificationToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) return false;

        EmailVerificationToken verificationToken = optionalToken.get();
        if (verificationToken.isExpired()) return false;

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        return true;
    }
}