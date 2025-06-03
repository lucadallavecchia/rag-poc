# 🧠 Spring Boot RAG PoC (Retrieval-Augmented Generation) with Ollama + Mistral

This is a Proof of Concept (PoC) for a RAG (Retrieval-Augmented Generation) system built with Java 21 and Spring Boot.  
It integrates a local LLM via Ollama (Mistral) with an in-memory vector store, allowing you to ask questions about a specific context.

---

## 📌 Features

- ✅ REST API `POST /ask` to receive user questions
- 🚧  In-memory vector store (`InMemoryVectorStore`)
- ✅ Local Ollama (Mistral model) integration via Spring AI
- 🚧  Automatic text chunking for better semantic retrieval

---

## 🧱 Tech Stack

| Technology     | Description |
|----------------|-------------|
| Java 21        | Programming language |
| Spring Boot    | Backend framework |
| Spring AI      | For LLM + embeddings integration |
| Ollama         | Local LLM runtime |
| Mistral        | LLM model used to generate responses |

---

## 🚀 Project Setup

### 🔧 Requirements

- Java 21
- Docker (for Ollama runtime)
- Ollama installed locally with the `mistral` model
