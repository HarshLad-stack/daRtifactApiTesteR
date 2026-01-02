package com.example.dartifactapitester.Service;

import com.example.dartifactapitester.DTO.StressTestRequest;
import com.example.dartifactapitester.DTO.VerdictResponse;
import com.example.dartifactapitester.Util.MetricsAnalyzer;
import com.example.dartifactapitester.Util.MetricsCollector;
import com.example.dartifactapitester.Util.VerdictEngine;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class StressTestService {

    private static final HttpClient httpClient =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

    public VerdictResponse execute(StressTestRequest request) {

        // ===== STEP 5: Safety caps =====
        int maxConcurrency = 100000; // hard cap
        int maxDuration = 60;     // seconds

        int concurrency = Math.min(request.getConcurrency(), maxConcurrency);
        int duration = Math.min(request.getDurationSeconds(), maxDuration);

        if (request.getConcurrency() > maxConcurrency || request.getDurationSeconds() > maxDuration) {
            throw new IllegalArgumentException(
                    "Concurrency or duration too high. Reduce values to safe limits."
            );
        }

        // ===== Metrics & Executor setup =====
        MetricsCollector metrics = new MetricsCollector();
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        long endTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(duration);

        // Submit worker threads
        for (int i = 0; i < concurrency; i++) {
            executor.submit(() -> runWorker(request, metrics, endTime));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(duration + 5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ===== Analyze metrics for summary =====
        MetricsAnalyzer analyzer = new MetricsAnalyzer(metrics);

        // ===== Verdict Engine =====
        VerdictEngine.VerdictResult result = new VerdictEngine().evaluate(metrics);

        // ===== Build Response =====
        VerdictResponse.Summary summary = new VerdictResponse.Summary();
        summary.setTotalRequests(metrics.getTotalRequests());
        summary.setAvgLatencyMs(analyzer.getAverageLatency());
        summary.setP95LatencyMs(analyzer.getP95Latency());
        summary.setErrorRate(analyzer.getErrorRate());

        VerdictResponse response = new VerdictResponse();
        response.setSummary(summary);
        response.setVerdict(result.getVerdict());
        response.setCommentary(result.getCommentary());

        return response;
    }

    // ===== Worker Thread =====
    private void runWorker(StressTestRequest config, MetricsCollector metrics, long endTime) {
        HttpRequest httpRequest = buildRequest(config);

        while (System.nanoTime() < endTime) {
            long start = System.nanoTime();
            try {
                HttpResponse<String> response = httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString()
                );

                long latencyMs = (System.nanoTime() - start) / 1_000_000;
                int status = response.statusCode();

                if (status >= 200 && status < 400) {
                    metrics.recordSuccess(latencyMs);
                } else if (status == 429) { // Rate-limit
                    metrics.recordRateLimit(latencyMs);
                } else {
                    metrics.recordFailure(latencyMs);
                }

            } catch (Exception e) {
                long latencyMs = (System.nanoTime() - start) / 1_000_000;
                metrics.recordFailure(latencyMs);
            }
        }
    }

    // ===== Build HttpRequest =====
    private HttpRequest buildRequest(StressTestRequest config) {
        HttpRequest.Builder builder =
                HttpRequest.newBuilder()
                        .uri(URI.create(config.getUrl()))
                        .timeout(Duration.ofSeconds(10));

        if (config.getHeaders() != null) {
            config.getHeaders().forEach(builder::header);
        }

        String method = config.getMethod().toUpperCase();

        switch (method) {
            case "POST", "PUT", "PATCH" -> builder.method(
                    method,
                    HttpRequest.BodyPublishers.ofString(
                            config.getBody() == null ? "" : config.getBody()
                    )
            );
            default -> builder.method(
                    method,
                    HttpRequest.BodyPublishers.noBody()
            );
        }

        return builder.build();
    }
}
