package com.okuylu_back.security.service;

import com.okuylu_back.security.dto.request.ManagerRegistrationDto;
import com.okuylu_back.security.dto.request.UserLoginDto;
import com.okuylu_back.security.dto.request.UserRegistrationDto;
import com.okuylu_back.security.dto.responses.AuthenticationResponse;
import com.okuylu_back.security.entity.Role;
import com.okuylu_back.model.User;
import com.okuylu_back.repository.RoleRepository;
import com.okuylu_back.repository.UserRepository;
import com.okuylu_back.security.jwt.JwtUtil;
import com.okuylu_back.security.entity.PersonDetails;
import com.okuylu_back.utils.ErrorsUtil;
import com.okuylu_back.utils.validators.PersonValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PersonValidator personValidator;
    private final EmailVerificationService emailVerificationService;



    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtils, AuthenticationManager authenticationManager, PersonValidator personValidator, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.personValidator = personValidator;
        this.emailVerificationService = emailVerificationService;
    }


    public AuthenticationResponse registerUser(UserRegistrationDto registrationDto, BindingResult bindingResult) {
        // Проверяем, существует ли пользователь с таким email
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));

        String defaultName = registrationDto.getEmail().split("@")[0];
        user.setUsername(defaultName);

        Role userRole = roleRepository.findByRoleName("USER");
        user.setRole(userRole);
        user.setEmailVerified(false); // Почта еще не подтверждена


        personValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnPersonError("Some fields are invalid.", bindingResult, HttpStatus.FORBIDDEN);
        }

        userRepository.save(user);
        emailVerificationService.createVerificationToken(user);

        String role = user.getRole().getRoleName();

        return new AuthenticationResponse("Please verify your email. Check your inbox.", role);

    }



    public AuthenticationResponse authenticateUser(UserLoginDto userLoginDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnPersonError("Some fields are invalid.", bindingResult, HttpStatus.FORBIDDEN);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
        } catch (BadCredentialsException e) {
            ErrorsUtil.returnPersonError("Login or password is incorrect.", bindingResult, HttpStatus.FORBIDDEN);
        }

        Optional<User> person = userRepository.findByEmail(userLoginDTO.getEmail());

        if (person.isEmpty()) {
            ErrorsUtil.returnPersonError("Person with such email is not found. Please check the input fields.", bindingResult, HttpStatus.NOT_FOUND);
        }

        if (!person.get().isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email is not verified. Please check your email.");
        }

        String jwtToken = jwtUtils.generateToken(new PersonDetails(person.get()));
        String role = person.get().getRole().getRoleName();

        return new AuthenticationResponse(jwtToken, role);
    }

    public AuthenticationResponse registerManager(ManagerRegistrationDto registrationDto, BindingResult bindingResult) {
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPhone(registrationDto.getPhone());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));

        String defaultName = registrationDto.getEmail().split("@")[0];
        user.setUsername(defaultName);

        Role managerRole = roleRepository.findByRoleName("MANAGER");
        if (managerRole == null) {
            throw new RuntimeException("Role MANAGER not found in database");
        }
        user.setRole(managerRole);
        user.setEmailVerified(false); // Почта еще не подтверждена

        personValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnPersonError("Some fields are invalid.", bindingResult, HttpStatus.FORBIDDEN);
        }

        userRepository.save(user);
        emailVerificationService.createVerificationToken(user);

        return new AuthenticationResponse("Please verify your email. Check your inbox.", user.getRole().getRoleName());
    }


}
