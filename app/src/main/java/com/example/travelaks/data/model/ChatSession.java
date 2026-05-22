package com.example.travelaks.data.model;

import com.google.firebase.Timestamp;

public class ChatSession {
    private String userId;
    private String city;
    private Timestamp startedAt;

    public ChatSession() {}

    public ChatSession(String userId, String city, Timestamp startedAt) {
        this.userId = userId;
        this.city = city;
        this.startedAt = startedAt;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Timestamp getStartedAt() { return startedAt; }
    public void setStartedAt(Timestamp startedAt) { this.startedAt = startedAt; }
}
