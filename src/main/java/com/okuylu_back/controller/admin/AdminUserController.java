package com.okuylu_back.controller.admin;

import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.dto.responses.UserResponse;
import com.okuylu_back.security.service.AuthService;
import com.okuylu_back.security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Users Controller")
public class AdminUserController {

    private final UserService userService;
    private final AuthService authService;


    public AdminUserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }



    @Operation(summary = "Получить список пользователей с пагинацией")
    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }


    @Operation(summary = "Изменить статус блокировки пользователя")
    @PatchMapping("/users/{userId}/block-status")
    public ResponseEntity<String> updateBlockStatus(
            @PathVariable Long userId,
            @RequestParam Boolean blocked) {
        userService.updateBlockStatus(userId, blocked);
        String message = blocked ? "User blocked successfully" : "User unblocked successfully";
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }



}
