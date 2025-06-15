package com.ldv.rag_poc.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AskRequest {

    @NotBlank(message = "Question cannot be empty.")
    private String question;
    private String context;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
