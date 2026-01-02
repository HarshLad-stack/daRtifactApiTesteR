package com.example.dartifactapitester.DTO;

public class VerdictResponse {

    private String verdict;
    private Summary summary;
    private String commentary;

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }



    public static class Summary {
        private long totalRequests;
        private String errorRate;
        private long avgLatencyMs;
        private long p95LatencyMs;

        public long getTotalRequests() {
            return totalRequests;
        }

        public void setTotalRequests(long totalRequests) {
            this.totalRequests = totalRequests;
        }

        public String getErrorRate() {
            return errorRate;
        }

        public void setErrorRate(String errorRate) {
            this.errorRate = errorRate;
        }

        public long getAvgLatencyMs() {
            return avgLatencyMs;
        }

        public void setAvgLatencyMs(long avgLatencyMs) {
            this.avgLatencyMs = avgLatencyMs;
        }

        public long getP95LatencyMs() {
            return p95LatencyMs;
        }

        public void setP95LatencyMs(long p95LatencyMs) {
            this.p95LatencyMs = p95LatencyMs;
        }
// getters & setters
    }
}
