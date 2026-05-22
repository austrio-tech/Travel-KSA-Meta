package com.example.travelaks.data.model;

import com.google.firebase.Timestamp;

public class Message {
    private String text;
    private boolean isUser;
    private Timestamp timestamp;

    public Message() {}

    public Message(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
        this.timestamp = Timestamp.now();
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isUser() { return isUser; }
    public void setUser(boolean user) { isUser = user; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}