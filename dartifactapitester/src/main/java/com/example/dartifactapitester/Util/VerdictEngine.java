package com.example.dartifactapitester.Util;

public class VerdictEngine {

    public static class VerdictResult {
        private final String verdict;
        private final String commentary;

        public VerdictResult(String verdict, String commentary) {
            this.verdict = verdict;
            this.commentary = commentary;
        }

        public String getVerdict() {
            return verdict;
        }

        public String getCommentary() {
            return commentary;
        }
    }

    /**
     * Evaluate metrics and return verdict + commentary.
     * Uses MetricsCollector to compute averages, p95 latency, error rate, and rate-limit awareness.
     *
     * @param metrics MetricsCollector instance with all request stats
     * @return VerdictResult containing verdict and commentary
     */
    public VerdictResult evaluate(MetricsCollector metrics) {

        long total = metrics.getTotalRequests();
        if (total == 0) {
            return new VerdictResult("PENDING", "No requests were executed.");
        }

        // Rate-limit awareness
        double rateLimitRate = (double) metrics.getRateLimitCount() / total * 100;

        // Error rate
        double errorRate = (double) metrics.getFailureCount() / total * 100;

        // Latency calculations
        MetricsAnalyzer analyzer = new MetricsAnalyzer(metrics);
        long avgLatency = analyzer.getAverageLatency();
        long p95Latency = analyzer.getP95Latency();

        // ===== Verdict logic =====
        if (rateLimitRate > 30.0) {
            return new VerdictResult(
                    "RATE_LIMITED",
                    "Endpoint enforced rate limits. Test exceeded allowed request rate."
            );
        }

        if (errorRate > 10.0) {
            return new VerdictResult(
                    "GUILTY",
                    "High error rate. Endpoint is failing under load."
            );
        }

        if (p95Latency > 2000) {
            return new VerdictResult(
                    "ON_THIN_ICE",
                    "Latency spikes observed under sustained load."
            );
        }

        if (p95Latency > avgLatency * 3) {
            return new VerdictResult(
                    "FLAKY",
                    "Significant latency variance detected. Endpoint response is inconsistent."
            );
        }

        return new VerdictResult(
                "STABLE",
                "Endpoint handled load without errors or significant latency."
        );
    }
}
