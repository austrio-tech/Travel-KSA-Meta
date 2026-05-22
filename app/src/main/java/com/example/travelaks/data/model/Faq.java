package com.example.travelaks.data.model;

public class Faq {
    private String question;
    private String answer;
    private int order;
    private boolean isVisible;

    public Faq() {}

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }

    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
}
