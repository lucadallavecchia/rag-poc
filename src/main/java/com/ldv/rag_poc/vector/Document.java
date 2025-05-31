package com.ldv.rag_poc.vector;

public class Document {

    String text;
    double[] embedding;

    public Document(String text, double[] embedding) {
        this.text = text;
        this.embedding = embedding;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(double[] embedding) {
        this.embedding = embedding;
    }
}
