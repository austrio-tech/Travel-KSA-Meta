package com.example.travelaks.data.model;

import com.google.firebase.Timestamp;

public class Message {
    private String text;
    private boolean isUser;
    private Timestamp timestamp;
    private boolean isTyping; // true = show typing indicator instead of text

    public Message() {}

    public Message(String text, boolean isUser) {
        this.text      = text;
        this.isUser    = isUser;
        this.timestamp = Timestamp.now();
        this.isTyping  = false;
    }

    /** Factory method — creates the animated "..." indicator shown while awaiting a response. */
    public static Message typingIndicator() {
        Message m = new Message();
        m.isTyping = true;
        return m;
    }

    public String getText()  { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isUser()  { return isUser; }
    public void setUser(boolean user) { isUser = user; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp ts) { this.timestamp = ts; }

    public boolean isTyping() { return isTyping; }
    public void setTyping(boolean typing) { isTyping = typing; }
}
