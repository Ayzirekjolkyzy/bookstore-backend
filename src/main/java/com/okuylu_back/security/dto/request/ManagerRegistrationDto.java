package com.okuylu_back.security.dto.request;

import jakarta.validation.constraints.*;

public class ManagerRegistrationDto {

    @NotBlank(message = "Email must contain at least 1 character")
    @Size(max = 100, message = "Email length must not exceed 100 characters")
    @Email(message = "This field must be formatted as Email address")
    private String email;


    @NotBlank(message = "Password must contain at least 10 characters")
    @Size(min = 10, max = 30, message = "Password length must be 10 - 30 characters")
    private String password;


    @NotNull(message = "Телефон нөмерү талап кылынат")
    @Pattern(regexp = "^(\\+996|0)?\\d{9}$", message = "Invalid phone number format")
    private String phone;


    public @NotBlank(message = "Email must contain at least 1 character") @Size(max = 100, message = "Email length must not exceed 100 characters") @Email(message = "This field must be formatted as Email address") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email must contain at least 1 character") @Size(max = 100, message = "Email length must not exceed 100 characters") @Email(message = "This field must be formatted as Email address") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password must contain at least 10 characters") @Size(min = 10, max = 30, message = "Password length must be 10 - 30 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password must contain at least 10 characters") @Size(min = 10, max = 30, message = "Password length must be 10 - 30 characters") String password) {
        this.password = password;
    }

    public @NotNull(message = "Телефон нөмерү талап кылынат") @Pattern(regexp = "^(\\+996|0)?\\d{9}$", message = "Invalid phone number format") String getPhone() {
        return phone;
    }

    public void setPhone(@NotNull(message = "Телефон нөмерү талап кылынат") @Pattern(regexp = "^(\\+996|0)?\\d{9}$", message = "Invalid phone number format") String phone) {
        this.phone = phone;
    }

    public ManagerRegistrationDto(String email, String password, String phone) {
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
