package com.okuylu_back.security.controller;
import com.okuylu_back.security.dto.request.UpdateUserProfileDto;
import com.okuylu_back.security.dto.request.UserLoginDto;
import com.okuylu_back.security.dto.request.UserRegistrationDto;
import com.okuylu_back.security.dto.responses.AuthenticationResponse;
import com.okuylu_back.security.dto.request.UserProfileDto;
import com.okuylu_back.model.User;
import com.okuylu_back.security.entity.PersonDetails;
import com.okuylu_back.security.service.AuthService;
import com.okuylu_back.security.service.EmailService;
import com.okuylu_back.security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication Controller")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private final EmailService emailMsgSender;;

    public AuthController(EmailService emailMsgSender) {
        this.emailMsgSender = emailMsgSender;
    }

    @Operation(summary = "Register a new user.",
            description = "Creates a new user entity and adds it into a DataBase. Requires a valid PersonRegistrationDTO object as a request body. Returns a valid JWT token for new authenticated user.")
    @PostMapping("/auth/register")

    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody  UserRegistrationDto userRegistrationDto,
                                                           BindingResult bindingResult) {

        AuthenticationResponse responseBody = authService.registerUser(userRegistrationDto, bindingResult);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate an existing user.",
            description = "Looks for a provided user credentials in a DataBase. Requires a valid PersonLoginDTO object as a request body. Returns a valid JWT token for authenticated user.")
    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid UserLoginDto userLoginDTO,
                                                               BindingResult bindingResult) {

        AuthenticationResponse responseBody = authService.authenticateUser(userLoginDTO, bindingResult);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "Update user profile.",
            description = "Updates the profile data of the currently authenticated user.")
    @PutMapping("/user/profile")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @AuthenticationPrincipal PersonDetails userDetails,
            @Valid @RequestBody UpdateUserProfileDto updateDto) {

        User updatedUser = userService.updateUser(userDetails.getUser().getUserId(), updateDto);
        return ResponseEntity.ok(new UserProfileDto(updatedUser));
    }

    @Operation(summary = "Get user profile.",
            description = "Returns the profile data of the currently authenticated user.")
    @GetMapping("/user/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal PersonDetails userDetails) {
        User user = userService.getUserById(userDetails.getUser().getUserId());
        UserProfileDto userProfile = new UserProfileDto(user);
        return ResponseEntity.ok(userProfile);
    }



}
