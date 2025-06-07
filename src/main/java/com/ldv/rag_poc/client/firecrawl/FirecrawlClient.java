package com.ldv.rag_poc.client.firecrawl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "firecrawlClient", url = "https://api.firecrawl.dev/v1")
public interface FirecrawlClient {
    @PostMapping(value = "/scrape", consumes = "application/json")
    FirecrawlResponse scrape(
            @RequestHeader("Authorization") String authorization,
            @RequestBody FirecrawlRequest request
    );
}

