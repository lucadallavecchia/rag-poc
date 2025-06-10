# 🧠 Spring Boot RAG PoC (Retrieval-Augmented Generation) with Ollama + Mistral

This is a Proof of Concept (PoC) for a RAG (Retrieval-Augmented Generation) system built with Java 21 and Spring Boot.  
It integrates a local LLM via Ollama (Mistral) with an in-memory vector store and all-MiniLM-L6-v2 embedding model (via Djl), allowing you to ask questions about a specific context.
The context is provided by documents that can be added via URLs, which are then processed to extract their content.

---

## 📖 Overview Apis
- ✅ REST API `POST /ask` to receive user questions
- ✅ REST API `GET /documents` to list all documents
- ✅ REST API `POST /documents` to add a new document by URL

## 📌 Features

- ✅ Firecrawler integration to get the markdown of a web page
- ✅ Embedding (Djl with all-MiniLM-L6-v2 model)
- 🚧 In-memory vector store (`InMemoryVectorStore`)
- ✅ Local Ollama (Mistral model) integration via Spring AI
- 🚧  Automatic text chunking for better semantic retrieval

---

## 🧱 Tech Stack

| Technology  | Description                               |
|-------------|-------------------------------------------|
| Java 21     | Programming language                      |
| Spring Boot | Backend framework                         |
| Firecrawl   | Service to retrieve the markdown of a url |
| Spring AI   | For LLM + embeddings integration          |
| Djl         | For embeddings (all-MiniLM-L6-v2)         |
| Ollama      | Local LLM runtime                         |
| Mistral     | LLM model used to generate responses      |

---

## 🚀 Project Setup

### 🔧 Requirements

- Java 21
- Gradle (use the wrapper in the codebase)
- Firecrawl API key (for web page retrieval)
- Ollama installed locally with the `mistral` model
