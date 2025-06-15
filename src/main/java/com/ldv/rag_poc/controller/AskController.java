package com.ldv.rag_poc.controller;

import com.ldv.rag_poc.client.ollama.OllamaClient;
import com.ldv.rag_poc.dto.request.AskRequest;
import com.ldv.rag_poc.vector.InMemoryVectorStore;
import jakarta.validation.Valid;
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
    public Map<String, String> ask(@RequestBody @Valid final AskRequest request) {

        logger.info("Received question: {}", request.getQuestion());

        try {
            var relevantDocs = vectorStore.findRelevant(request.getQuestion(), topK);
            logger.debug("Found {} relevant documents", relevantDocs.size());

            request.setContext(String.join("\n", relevantDocs));
            logger.debug("Context: {}", request.getContext());

            String prompt = request.getContext().isEmpty()
                    ? request.getQuestion()
                    : ("Context: " + request.getContext() + "\nQuestion: " + request.getQuestion());

            String answer = ollamaClient.ask(prompt);
            return Collections.singletonMap("answer", answer);
        } catch (Exception e) {
            logger.error("Error processing ask request", e);
            return Collections.singletonMap("answer", "An error occurred while processing your question.");
        }
    }

}
