package com.ldv.rag_poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RagPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagPocApplication.class, args);
    }

}
