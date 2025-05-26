package com.ldv.rag_poc.controller;

import com.ldv.rag_poc.client.OllamaClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/ask")
public class AskController {

    private OllamaClient ollamaClient;

    public AskController(OllamaClient ollamaClient){
        this.ollamaClient = ollamaClient;
    }

    @PostMapping
    public Map<String,String> ask(@RequestBody String question) {
        String answer = ollamaClient.ask(question);
        return Collections.singletonMap("answer", answer);
    }

}
