package com.ldv.rag_poc.controller;

import com.ldv.rag_poc.client.ollama.OllamaClient;
import com.ldv.rag_poc.vector.InMemoryVectorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/ask")
public class AskController {

    Logger logger = LoggerFactory.getLogger(AskController.class);
    private int topK;
    private OllamaClient ollamaClient;
    private InMemoryVectorStore vectorStore;

    public AskController(OllamaClient ollamaClient,
                         InMemoryVectorStore vectorStore,
                         @Value("${number.of.top.chunks:3}") int topK) {
        this.ollamaClient = ollamaClient;
        this.vectorStore = vectorStore;
        this.topK = topK;
    }

    @PostMapping
    public Map<String, String> ask(@RequestBody String question) {

        logger.debug("Question: {}", question);
        // Find the top 5 most relevant documents → vector store initialized inside InMemoryVectorStore.java
        var relevantDocs = vectorStore.findRelevant(question, topK);
        // Create the context string
        String context = String.join("\n", relevantDocs);
        logger.debug("Context: {}", context);

        // Create the prompt: context + question
        String prompt = context.isEmpty() ? question : ("Context: " + context + "\nQuestion: " + question);
        // Call ollama
        String answer = ollamaClient.ask(prompt);

        // Answer
        return Collections.singletonMap("answer", answer);
    }

}
