package com.okuylu_back.security.service;

import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.dto.responses.UserResponse;
import com.okuylu_back.security.dto.request.ManagerProfileDto;
import com.okuylu_back.security.dto.request.UpdateUserProfileDto;
import com.okuylu_back.model.User;
import com.okuylu_back.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long userId, UpdateUserProfileDto updateDto) {
        User user = getUserById(userId);

        if (updateDto.getUsername() != null) {
            user.setUsername(updateDto.getUsername());
        }
        if (updateDto.getBirthDate() != null) {
            user.setBirthDate(updateDto.getBirthDate());
        }
        if (updateDto.getGender() != null) {
            user.setGender(updateDto.getGender());
        }

        return userRepository.save(user);
    }

    public void updateBlockStatus(Long userId, boolean blocked) {
        User user = getUserById(userId);
        if (!"USER".equals(user.getRole().getRoleName())) {
            throw new RuntimeException("Person is not a user");
        }
        user.setBlocked(blocked);
        userRepository.save(user);
    }



    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    @Transactional
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<User> usersPage = userRepository.findByRole_RoleName("USER", pageable);

        Page<UserResponse> dtoPage = usersPage.map(this::convertToDto);

        return PageResponse.fromPage(dtoPage);
    }



    @Transactional
    public PageResponse<ManagerProfileDto> getAllManagers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<User> managersPage = userRepository.findByRole_RoleName("MANAGER", pageable);

        Page<ManagerProfileDto> dtoPage = managersPage.map(ManagerProfileDto::new);

        return PageResponse.fromPage(dtoPage);
    }

    public UserResponse getManagerById(Long managerId) {
        User manager = getUserById(managerId);
        if (!manager.getRole().getRoleName().equals("MANAGER")) {
            throw new RuntimeException("User is not a manager");
        }
        return convertToDto(manager);
    }

    private UserResponse convertToDto(User user) {
        UserResponse dto = new UserResponse();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRoleName(user.getRole().getRoleName()); // Получаем имя роли
        dto.setEmailVerified(user.getEmailVerified());
        dto.setIsBlocked(user.getIsBlocked());
        return dto;
    }

    public void updateManagerBlockStatus(Long managerId, boolean blocked) {
        User manager = getUserById(managerId);

        if (!"MANAGER".equals(manager.getRole().getRoleName())) {
            throw new RuntimeException("User is not a manager");
        }

        manager.setBlocked(blocked);
        userRepository.save(manager);
    }


    public void deleteManager(Long managerId) {
        User manager = getUserById(managerId);
        if (!manager.getRole().getRoleName().equals("MANAGER")) {
            throw new RuntimeException("User is not a manager");
        }
        manager.setBlocked(false);
        userRepository.deleteById(managerId);
    }

    public User updateManager(Long managerId, ManagerProfileDto updateDto) {
        User manager = getUserById(managerId);

        if (!manager.getRole().getRoleName().equals("MANAGER")) {
            throw new RuntimeException("User is not a manager");
        }

        if (updateDto.getUsername() != null) {
            manager.setUsername(updateDto.getUsername());
        }
        if (updateDto.getEmail() != null) {
            manager.setEmail(updateDto.getEmail());
        }

        if (updateDto.getPhone() != null) {
            manager.setPhone(updateDto.getPhone());
        }
        if (updateDto.getPassword() != null) {
            manager.setPasswordHash(passwordEncoder.encode(updateDto.getPassword()));
        }

        return userRepository.save(manager);
    }

}
