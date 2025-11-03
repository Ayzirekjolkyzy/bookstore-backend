package com.okuylu_back.security.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.okuylu_back.model.User;

import java.time.LocalDate;
import java.util.Objects;

public class UserProfileDto {
    private Long userId;
    private String email;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Boolean gender;


    public UserProfileDto(Long userId, String email, String username, LocalDate birthDate, Boolean gender) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public UserProfileDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
    }

    public UserProfileDto() {
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserProfileDto{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileDto that = (UserProfileDto) o;
        return Objects.equals(userId, that.userId) && Objects.equals(email, that.email) && Objects.equals(username, that.username) && Objects.equals(birthDate, that.birthDate) && Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, username, birthDate, gender);
    }
}