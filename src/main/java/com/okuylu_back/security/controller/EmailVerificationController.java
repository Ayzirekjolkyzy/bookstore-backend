package com.okuylu_back.security.controller;

import com.okuylu_back.model.EmailVerificationToken;
import com.okuylu_back.model.User;
import com.okuylu_back.repository.EmailVerificationTokenRepository;
import com.okuylu_back.repository.UserRepository;
import com.okuylu_back.security.service.EmailVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class EmailVerificationController {

    private final EmailVerificationService verificationService;
    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;




    public EmailVerificationController(EmailVerificationService verificationService, EmailVerificationTokenRepository tokenRepository, UserRepository userRepository) {
        this.verificationService = verificationService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token, Model model) {
        Optional<EmailVerificationToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            model.addAttribute("message", "Неверная или устаревшая ссылка.");
            return "email-verification-failed";
        }

        EmailVerificationToken verificationToken = optionalToken.get();

        if (verificationToken.isExpired()) {
            tokenRepository.delete(verificationToken);
            model.addAttribute("message", "Срок действия ссылки истёк. Запросите новое письмо.");
            model.addAttribute("email", verificationToken.getUser().getEmail());
            return "email-verification-expired";
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken); // Тут уже можно удалить по объекту User

        return "email-verified";
    }


    @PostMapping("/resend-verification")
    @ResponseBody
    public ResponseEntity<String> resendVerification(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Пользователь не найден.");
        }

        User user = optionalUser.get();

        if (user.getEmailVerified()) {
            return ResponseEntity.badRequest().body("Email уже подтверждён.");
        }

        verificationService.createVerificationToken(user);
        return ResponseEntity.ok("Новое письмо отправлено.");
    }


}
