package com.okuylu_back.model;

import com.okuylu_back.security.entity.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified = false;

    public User() {
    }

    public User(Long userId, String email, String username,
                String phone, String passwordHash, Role role,
                LocalDate birthDate, Timestamp createdAt, Boolean gender,
                Timestamp updatedAt, Boolean isBlocked, Boolean isEmailVerified) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.gender = gender;
        this.updatedAt = updatedAt;
        this.isBlocked = isBlocked;
        this.isEmailVerified = isEmailVerified;
    }

    public Boolean isEmailVerified() { return isEmailVerified; }

    public void setIsEmailVerified(Boolean emailVerified) {  isEmailVerified = emailVerified; }

    public Boolean getEmailVerified() { return isEmailVerified; }

    public void setEmailVerified(Boolean emailVerified) {  isEmailVerified = emailVerified; }

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + role +
                ", birthDate=" + birthDate +
                ", createdAt=" + createdAt +
                ", gender=" + gender +
                ", updatedAt=" + updatedAt +
                ", isBlocked=" + isBlocked +
                ", isEmailVerified=" + isEmailVerified +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
