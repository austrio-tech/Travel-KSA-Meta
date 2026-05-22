package com.example.travelaks.data.model;

import com.google.firebase.Timestamp;

public class User {
    private String email;
    private Timestamp createdAt;
    private Timestamp lastLoginAt;
    private String role;

    public User() {}

    public User(String email, Timestamp createdAt, String role) {
        this.email = email;
        this.createdAt = createdAt;
        this.role = role;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Timestamp lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
