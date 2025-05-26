package com.ldv.rag_poc.client;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Component;


@Component
public class OllamaClient {

    private final OllamaChatModel ollamaChatModel;

    public OllamaClient(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    public String ask(String question) {

        Prompt prompt = new Prompt(
                new UserMessage(question),
                OllamaOptions.builder()
                        .model(OllamaModel.MISTRAL)
                        .build()
        );

        var response = ollamaChatModel.call(prompt);
        var resultMessage = response.getResult().getOutput();
        return resultMessage.getText();
    }
}

