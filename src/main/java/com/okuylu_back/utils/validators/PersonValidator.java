package com.okuylu_back.utils.validators;

import com.okuylu_back.model.User;
import com.okuylu_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public PersonValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User personForValidation = (User) target;

        Optional<User> personFromBD = userRepository.findByEmail(personForValidation.getEmail());

        if (personFromBD.isPresent()) {
            errors.rejectValue("email", "Person with this email is already registered");
        }
    }
}