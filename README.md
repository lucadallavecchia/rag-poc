# 🧠 Spring Boot RAG PoC (Retrieval-Augmented Generation) with Ollama + Mistral

This is a Proof of Concept (PoC) for a RAG (Retrieval-Augmented Generation) system built with Java 21 and Spring Boot.  
It integrates a local LLM via Ollama (Mistral) with an in-memory vector store and all-MiniLM-L6-v2 embedding model (via Djl), allowing you to ask questions about a specific context.
The context is provided by documents that can be added via URLs, which are then processed to extract their content.

---

## 📌 Features

- ✅ [Firecrawler](https://www.firecrawl.dev/) integration to get the markdown of a web page
- 🚧  Automatic text chunking for better semantic retrieval
- ✅ Embedding (Djl with [all-MiniLM-L6-v2](https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2) model)
- 🚧 In-memory vector store (`InMemoryVectorStore`)
- ✅ Local [Ollama](https://ollama.com/) ([Mistral](https://ollama.com/library/mistral) model) integration via Spring AI

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

## 🔧 Requirements

- Java 21
- Gradle (use the wrapper in the codebase)
- Ollama installed locally with the `mistral` model
- Firecrawl API key (for web page retrieval)

---

## 🚀 Project Setup

To setup the project, follow these steps:

### 1. Ollama & Mistral

To be able to run this project, you need to have **Ollama** installed locally and the **Mistral** model downloaded.

1. The easiest way is to use Homebrew on macOS; open the terminal and run:
```bash
  brew install ollama
```

2. To download the Mistral model(it will take a while) open the terminal and run:
```bash
  ollama run mistral
```

3. Now we are ready to start ollama locally (We are going to start ollama like a local backend server);  open the terminal and run: 
```bash
  ollama serve
```

Now you will have a local Ollama server running on `http://localhost:11434`.

### 2. Firecrawl - Authentication Key

To use Firecrawl, you need to sign up and get an API key.

1. You can sign up at [Firecrawl](https://www.firecrawl.dev/). 
2. Once you have the key replace the placeholder `[your-auth-token]` with your actual Firecrawl API key in [application.properties](src/main/resources/application.properties)

### 3. How to run the project

1. Build the project to check that you have all the dependencies; from the project root directory, run:
   ```bash
   ./gradlew build
   ```
2. Run the application (It will take a while to start the first time as it will download the embedding model):
   ```bash
   ./gradlew bootRun
    ```
   
---

## ▶️ Usage
After starting the application and ollama locally (see "Project Setup"), you can use the provided APIs to interact with the system.

Suggested usage flow:
1. Search a web page (Using a page that talks about a specific actual topic will avoid having doubts regarding the pre-trained model knowledge cutoff date 😉)
2. Use the `POST /documents` api to generate documents starting from a url (It will embed the content of the page in the vector store)
3. Use the `GET /documents` api to list all the documents in the vector store (debugging purposes) to check that the url has been embedded and added correctly
4. Use the `POST /ask` api to ask questions about the context (the documents embedded in the vector store)

Suggestions 🙂:
- Try to use a web page related to an actual topic (e.g "The last player purchased by your favorite team")
- Ask question about the content of the page you have added (e.g. "Who is [name of the player]?")
- Try the `POST /ask` api with the same question **BEFORE** and **AFTER** adding processing a url to see the difference in the answers (the first time it will use the pre-trained model knowledge, the second time it will use the context provided by the document)

### 📖 Overview Apis

You can download the postman collection from the **resources** directory of the project to test the APIs: [RAG-POC.postman_collection.json](src/main/resources/static/RAG-POC.postman_collection.json)

#### Apis available:
- ✅ REST API `POST /ask` to **ask** questions about the context
- ✅ REST API `POST /documents` to **add** a new document by URL (to add a document, you need to provide a valid URL in the request body)
- ✅ REST API `GET /documents` to **list** all documents (the documents embedded in the vector store)

In  the postman collecation you will find examples of direct integration with Ollama and Firecrawl APIs:
- ✅ REST API `POST /api/generate` to **ask** questions to ollama directly
- ✅ REST API `POST /v1/scrape` to **get** the markdown of a web page directly from Firecrawl
