package com.example.travelaks.data.model;

public class Message {
    private String text;
    private boolean isUser;

    public Message(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }

    public String getText() {
        return text;
    }

    public boolean isUser() {        // ← غيرناها إلى isUser() عشان تطابق مع الـ Adapter
        return isUser;
    }
}