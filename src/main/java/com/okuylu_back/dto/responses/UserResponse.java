package com.okuylu_back.dto.responses;

public class UserResponse {

    private Long userId;
    private String email;
    private String username;
    private String roleName;
    private Boolean isBlocked;
    private Boolean isEmailVerified;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean blocked) {
        isBlocked = blocked;
    }



    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public UserResponse() {
    }

    public UserResponse(Long userId, String email, String username, String roleName, Boolean isBlocked, Boolean isEmailVerified) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.roleName = roleName;
        this.isBlocked = isBlocked;
        this.isEmailVerified = isEmailVerified;
    }
}
