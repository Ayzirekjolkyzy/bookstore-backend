package com.okuylu_back.security.dto.request;

import com.okuylu_back.model.User;

public class ManagerProfileDto {
    private Long userId;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String roleName;
    private Boolean isBlocked;
    private Boolean isEmailVerified;

    public ManagerProfileDto(User updatedUser) {
        this.userId = updatedUser.getUserId();
        this.email = updatedUser.getEmail();
        this.phone=updatedUser.getPhone();
        this.username = updatedUser.getUsername();
        this.password=updatedUser.getPasswordHash();
        this.roleName=updatedUser.getRole().getRoleName();
        this.isBlocked=updatedUser.getIsBlocked();
        this.isEmailVerified=updatedUser.isEmailVerified();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ManagerProfileDto(Long userId, String email, String phone, String username, String password, String roleName, Boolean isBlocked, Boolean isEmailVerified) {
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.roleName = roleName;
        this.isBlocked = isBlocked;
        this.isEmailVerified = isEmailVerified;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public ManagerProfileDto() {

    }
}
