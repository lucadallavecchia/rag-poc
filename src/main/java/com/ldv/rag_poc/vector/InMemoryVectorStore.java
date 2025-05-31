package com.ldv.rag_poc.vector;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class InMemoryVectorStore {

    private final List<Document> documents = new ArrayList<>();

    // Mock embedding: convert string to vector (replace with real embedding logic)
    private double[] embed(String text) {
        double[] vec = new double[10];
        for (int i = 0; i < text.length() && i < 10; i++) {
            vec[i] = (double) text.charAt(i);
        }
        return vec;
    }

    public void addDocument(String text) {
        documents.add(new Document(text, embed(text)));
    }

    // Cosine similarity
    private double cosineSim(double[] a, double[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10);
    }

    public List<String> findRelevant(String query, int topK) {
        double[] qVec = embed(query);
        PriorityQueue<Document> pq = new PriorityQueue<>(Comparator.comparingDouble(d -> -cosineSim(qVec, d.embedding)));
        pq.addAll(documents);
        List<String> results = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < topK) {
            results.add(pq.poll().text);
            count++;
        }
        return results;
    }

    //Seed iniziale
    @PostConstruct
    public void initSampleDocs() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("static/documents.txt")) {
            if (is == null) {
                System.err.println("documents.txt not found");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank()) {
                        addDocument(line.trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Document> getDocuments() {
        return documents;
    }
}

