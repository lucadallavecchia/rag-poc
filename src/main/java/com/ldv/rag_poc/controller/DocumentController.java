package com.ldv.rag_poc.controller;

import com.ldv.rag_poc.client.firecrawl.FirecrawlClient;
import com.ldv.rag_poc.client.firecrawl.FirecrawlRequest;
import com.ldv.rag_poc.client.firecrawl.FirecrawlResponse;
import com.ldv.rag_poc.vector.Document;
import com.ldv.rag_poc.vector.InMemoryVectorStore;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * This controller is just for testing purpose -> to evaluate the chunk generation.
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    Logger logger = LoggerFactory.getLogger(DocumentController.class);


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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @PostMapping
    public void addDocument(@RequestBody UrlRequest request) {
        this.scrapeDocument(request.getUrl());
    }

    private void scrapeDocument(String url) {

        if (!StringUtil.isBlank(url)) {

            logger.info("Calling Firecrawl for URL: {} ...", url);
            // Call firecrawl
            List<String> formats = List.of("markdown", "html");
            long currentTime = System.currentTimeMillis();
            FirecrawlResponse response = firecrawlClient.scrape(firecrawlAuthorizationToken, new FirecrawlRequest(url, formats));
            logger.info("Firecrawl Call duration: {} ms", (System.currentTimeMillis()) - currentTime);

            // debug
            logger.debug("Firecrawl Response:");
            logger.debug("Firecrawl status: {}", response.isSuccess());
            logger.debug("Firecrawl markdown: {}", response.getMarkdown());

            // add mark
            if (response.getMarkdown() != null) {
                logger.info("Adding document to vector store from Firecrawl response...");
                currentTime = System.currentTimeMillis();
                vectorStore.addDocumentFromMarkDown(response.getMarkdown());
                logger.info("Adding document to vector store duration: {} ms", (System.currentTimeMillis()) - currentTime);
            }
        }
    }
}
