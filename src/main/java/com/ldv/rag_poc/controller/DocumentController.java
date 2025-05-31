package com.ldv.rag_poc.controller;

import com.ldv.rag_poc.vector.Document;
import com.ldv.rag_poc.vector.InMemoryVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/***
 * This controller is just for testing purpose -> to evaluate the chunk generation.
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final InMemoryVectorStore vectorStore;

    public DocumentController(InMemoryVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping
    public Map<String, List<String>> getDocuments() {
        List<Document> documents = vectorStore.getDocuments();
        List<String> texts = documents.stream().map(document -> document.getText()).toList();
        return Map.of("documents", texts);
    }
}
