package com.ldv.rag_poc.client.firecrawl;

import java.util.List;

public class FirecrawlRequest {
    private String url;
    private List<String> formats;

    public FirecrawlRequest() {}

    public FirecrawlRequest(String url, List<String> formats) {
        this.url = url;
        this.formats = formats;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }
}

