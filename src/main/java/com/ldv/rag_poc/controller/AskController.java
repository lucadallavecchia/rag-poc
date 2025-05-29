package com.ldv.rag_poc.controller;

import com.ldv.rag_poc.client.OllamaClient;
import com.ldv.rag_poc.vector.InMemoryVectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/ask")
public class AskController {

    private OllamaClient ollamaClient;
    private InMemoryVectorStore vectorStore;

    public AskController(OllamaClient ollamaClient, InMemoryVectorStore vectorStore){
        this.ollamaClient = ollamaClient;
        this.vectorStore = vectorStore;
    }

    @PostMapping
    public Map<String,String> ask(@RequestBody String question) {
        // Trova i documenti più rilevanti (top 3) -> vector store inizializzato dentro InMemoryVectorStore.java
        var relevantDocs = vectorStore.findRelevant(question, 3);
        // Crea la stringa di contesto
        String context = String.join("\n", relevantDocs);
        // Crea il prompt: contesto + domanda
        String prompt = context.isEmpty() ? question : ("Context: " + context + "\nQuestion: " + question);
        // Chiama ollama
        String answer = ollamaClient.ask(prompt);
        // Risponde
        return Collections.singletonMap("answer", answer);
    }

}
