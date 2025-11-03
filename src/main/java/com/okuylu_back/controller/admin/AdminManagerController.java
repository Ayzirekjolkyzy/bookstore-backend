package com.okuylu_back.controller.admin;


import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.dto.responses.UserResponse;
import com.okuylu_back.model.User;
import com.okuylu_back.security.dto.request.ManagerProfileDto;
import com.okuylu_back.security.dto.request.ManagerRegistrationDto;
import com.okuylu_back.security.dto.responses.AuthenticationResponse;
import com.okuylu_back.security.service.AuthService;
import com.okuylu_back.security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Managers Controller")
public class AdminManagerController {

    private final UserService userService;
    private final AuthService authService;


    public AdminManagerController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    @Operation(summary = "Получить список всех менеджеров с пагинацией")
    @GetMapping("/managers")
    public ResponseEntity<PageResponse<ManagerProfileDto>> getAllManagers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getAllManagers(page, size));
    }

    @Operation(summary = "Изменить статус блокировки менеджера")
    @PatchMapping("/managers/{managerId}/block-status")
    public ResponseEntity<String> updateBlockStatus(
            @PathVariable Long managerId,
            @RequestParam Boolean blocked) {

        userService.updateManagerBlockStatus(managerId, blocked);

        String message = blocked ? "Manager blocked successfully" : "Manager unblocked successfully";
        return ResponseEntity.ok(message);
    }


    @DeleteMapping("/managers/{managerId}")
    public ResponseEntity<String> deleteManager(@PathVariable Long managerId) {
        userService.deleteManager(managerId);
        return ResponseEntity.ok("Manager deleted successfully");
    }

    @PostMapping("/managers/register")
    public ResponseEntity<AuthenticationResponse> registerManager(
            @RequestBody ManagerRegistrationDto registrationDto,
            BindingResult bindingResult
    ) {
        AuthenticationResponse response = authService.registerManager(registrationDto, bindingResult);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update manager profile.")
    @PatchMapping("/managers/{managerId}/profile")
    public ResponseEntity<ManagerProfileDto> updateUserProfile(
            @PathVariable Long managerId,
            @RequestBody ManagerProfileDto updateDto) {

        User updatedUser = userService.updateManager(managerId, updateDto);
        return ResponseEntity.ok(new ManagerProfileDto(updatedUser));
    }


}
