package com.ldv.rag_poc.vector;

import ai.djl.translate.TranslateException;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

import org.jsoup.Jsoup;

@Component
public class InMemoryVectorStore {

    private int chunkSize;
    private final List<Document> documents = new ArrayList<>();
    private CustomEmbedder embedder;

    public List<Document> getDocuments() {
        return documents;
    }

    public InMemoryVectorStore(CustomEmbedder embedder,
                               @Value("${phrases.per.chunk:1}") int chunkSize) {
        this.chunkSize = chunkSize;
        this.embedder = embedder;
    }

    private double[] embed(String text) {
        try {
            float[] emb = embedder.embed(text);
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
