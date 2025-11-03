package com.okuylu_back.security.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;

public class UpdateUserProfileDto {
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Boolean gender;




    @Override
    public String toString() {
        return "UpdateUserProfileDto{" +
                "username='" + username + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserProfileDto that = (UpdateUserProfileDto) o;
        return Objects.equals(username, that.username) && Objects.equals(birthDate, that.birthDate) && Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, birthDate, gender);
    }

    public @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") String username) {
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

    public UpdateUserProfileDto() {
    }

    public UpdateUserProfileDto(String username, LocalDate birthDate, Boolean gender) {
        this.username = username;
        this.birthDate = birthDate;
        this.gender = gender;
    }
}
