package com.ldv.rag_poc.client.firecrawl;

import java.util.Map;

public class FirecrawlResponse {
    private boolean success;
    private Data data;

    public static class Data {
        private String markdown;
        private String html;
        private Map<String, Object> metadata;

        public String getMarkdown() { return markdown; }
        public void setMarkdown(String markdown) { this.markdown = markdown; }
        public String getHtml() { return html; }
        public void setHtml(String html) { this.html = html; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    // Helper for backward compatibility (optional)
    public String getMarkdown() { return data != null ? data.getMarkdown() : null; }
    public String getHtml() { return data != null ? data.getHtml() : null; }
    public Map<String, Object> getMetadata() { return data != null ? data.getMetadata() : null; }
}
