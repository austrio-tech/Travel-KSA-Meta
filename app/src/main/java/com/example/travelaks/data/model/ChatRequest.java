package com.example.travelaks.data.model;

public class ChatRequest {
    public String question;
    public String session_id; // null to start a new session

    public ChatRequest(String question, String sessionId) {
        this.question = question;
        this.session_id = sessionId;
    }
}
