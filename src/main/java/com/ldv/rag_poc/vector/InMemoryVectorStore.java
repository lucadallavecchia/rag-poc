package com.ldv.rag_poc.vector;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import jakarta.annotation.PostConstruct;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;

@Component
public class InMemoryVectorStore {

    @Value("${phrases.per.chunk:1}")
    private int chunkSize;
    private final List<Document> documents = new ArrayList<>();
    private BertEmbedder bertEmbedder;

    public List<Document> getDocuments() {
        return documents;
    }

    public InMemoryVectorStore() {
        try {
            bertEmbedder = new BertEmbedder();
        } catch (ModelException | IOException e) {
            throw new RuntimeException("Failed to initialize BertEmbedder", e);
        }
    }

    private double[] embed(String text) {
        try {
            float[] emb = bertEmbedder.embed(text);
            double[] vec = new double[emb.length];
            for (int i = 0; i < emb.length; i++) {
                vec[i] = emb[i];
            }
            return vec;
        } catch (TranslateException e) {
            throw new RuntimeException("Embedding failed", e);
        }
    }

    public void addDocument(String text) {
        documents.add(new Document(text, embed(text)));
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

    public void addDocumentFromMarkDown(String markdown) {
        if (!StringUtil.isBlank(markdown)) {
            String cleanText = Jsoup.parse(markdown).text();
            // Chunking: suddividi per paragrafi o ogni N frasi (qui ogni 1 frasi)
            String[] sentences = cleanText.split("(?<=[.!?]) ");
            for (int i = 0; i < sentences.length; i += chunkSize) {
                StringBuilder chunk = new StringBuilder();
                for (int j = i; j < i + chunkSize && j < sentences.length; j++) {
                    chunk.append(sentences[j]).append(" ");
                }
                String chunkText = chunk.toString().trim();
                if (!chunkText.isBlank()) {
                    this.addDocument(chunkText);
                }
            }
        }
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

    /*@PostConstruct
    public void initSampleDocs() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("static/documents.txt")) {
            if (is == null) {
                System.err.println("documents.txt not found");
                return;
            }
            // Leggi tutto il file come una singola stringa
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            // Estrai solo il testo dal markup HTML/XML
            String cleanText = Jsoup.parse(sb.toString()).text();
            // Chunking: suddividi per paragrafi o ogni N frasi (qui ogni 1 frasi)
            String[] sentences = cleanText.split("(?<=[.!?]) ");
            for (int i = 0; i < sentences.length; i += chunkSize) {
                StringBuilder chunk = new StringBuilder();
                for (int j = i; j < i + chunkSize && j < sentences.length; j++) {
                    chunk.append(sentences[j]).append(" ");
                }
                String chunkText = chunk.toString().trim();
                if (!chunkText.isBlank()) {
                    addDocument(chunkText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
