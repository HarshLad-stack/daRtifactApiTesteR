package com.example.dartifactapitester.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

public class StressTestRequest {
    private String url;
    private String method;
    private int concurrency;

    @Override
    public String toString() {
        return "StressTestRequest{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", concurrency=" + concurrency +
                ", durationSeconds=" + durationSeconds +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private int durationSeconds;
    private Map<String ,String> headers;
    private String body;

}
