package com.ldv.rag_poc.vector;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryVectorStore {
    private static class Document {
        String text;
        double[] embedding;
        Document(String text, double[] embedding) {
            this.text = text;
            this.embedding = embedding;
        }
    }

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

    // ✅ Seed iniziale
    @PostConstruct
    public void initSampleDocs() {
        addDocument("The Colosseum is a Roman amphitheater located in Rome.");
        addDocument("The Sistine Chapel is famous for its ceiling painted by Michelangelo.");
        addDocument("The Eiffel Tower is located in Paris and is 330 meters tall.");
    }
}

