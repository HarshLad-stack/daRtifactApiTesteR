package com.example.dartifactapitester.Util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsCollector {
    private final AtomicLong totalRequests = new AtomicLong();
    private final AtomicLong successCount = new AtomicLong();
    private final AtomicLong failureCount = new AtomicLong();
    private final AtomicLong rateLimitCount = new AtomicLong(); // NEW
    private final ConcurrentLinkedQueue<Long> latencies = new ConcurrentLinkedQueue<>();

    public void recordSuccess(long latencyMs) {
        totalRequests.incrementAndGet();
        successCount.incrementAndGet();
        latencies.add(latencyMs);
    }

    public void recordFailure(long latencyMs) {
        totalRequests.incrementAndGet();
        failureCount.incrementAndGet();
        latencies.add(latencyMs);
    }

    public void recordRateLimit(long latencyMs) { // NEW
        totalRequests.incrementAndGet();
        rateLimitCount.incrementAndGet();
        latencies.add(latencyMs);
    }

    // ===== Getters =====
    public long getTotalRequests() {
        return totalRequests.get();
    }

    public long getSuccessCount() {
        return successCount.get();
    }

    public long getFailureCount() {
        return failureCount.get();
    }

    public long getRateLimitCount() { // NEW
        return rateLimitCount.get();
    }

    public ConcurrentLinkedQueue<Long> getLatencies() {
        return latencies;
    }
}
