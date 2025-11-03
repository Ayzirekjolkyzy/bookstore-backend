package com.okuylu_back.security.service;

import com.okuylu_back.repository.UserRepository;
import com.okuylu_back.security.entity.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import com.okuylu_back.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public PersonDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username.toLowerCase();

        Optional<User> person = userRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("No users found with such email.");
        }

        return new PersonDetails(person.get());
    }
}
