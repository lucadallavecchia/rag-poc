package com.ldv.rag_poc.controller;

import com.ldv.rag_poc.client.firecrawl.FirecrawlClient;
import com.ldv.rag_poc.client.firecrawl.FirecrawlRequest;
import com.ldv.rag_poc.client.firecrawl.FirecrawlResponse;
import com.ldv.rag_poc.vector.Document;
import com.ldv.rag_poc.vector.InMemoryVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/***
 * This controller is just for testing purpose -> to evaluate the chunk generation.
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Value("${firecrawl.authorization.token:null}")
    private String firecrawlAuthorizationToken;

    private final InMemoryVectorStore vectorStore;
    private final FirecrawlClient firecrawlClient;

    public DocumentController(InMemoryVectorStore vectorStore, FirecrawlClient firecrawlClient) {
        this.vectorStore = vectorStore;
        this.firecrawlClient = firecrawlClient;
    }

    @GetMapping
    public Map<String, List<String>> getDocuments() {
        List<Document> documents = vectorStore.getDocuments();
        List<String> texts = documents.stream().map(document -> document.getText()).toList();
        return Map.of("documents", texts);
    }

    public static class UrlRequest {
        private String url;
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    @PostMapping
    public void addDocument(@RequestBody UrlRequest request) {
        this.scrapeDocument(request.getUrl());
    }

    private FirecrawlResponse scrapeDocument(String url) {
        List<String> formats = List.of("markdown","html");

        FirecrawlResponse response = firecrawlClient.scrape(firecrawlAuthorizationToken, new FirecrawlRequest(url, formats));

        if (response.getMarkdown() != null) {
            //TODO embedding and then add documentvectorStore.addDocument(response.getMarkdown());
        }
        return response;
    }
}
