package com.example.dartifactapitester.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MetricsAnalyzer {
    private final MetricsCollector metrics;
    public MetricsAnalyzer(MetricsCollector metrics) {
        this.metrics = metrics;
    }
    public long getAverageLatency(){
        List<Long> latencies= new ArrayList<>(metrics.getLatencies());
        if (latencies.isEmpty()) return 0;
        long sum = 0;
        for (long l : latencies) sum += l;
        return sum / latencies.size();

    }
    public long getP95Latency() {
        List<Long> latencies = new ArrayList<>(metrics.getLatencies());
        if (latencies.isEmpty()) return 0;

        Collections.sort(latencies);
        int index = (int) Math.ceil(latencies.size() * 0.95) - 1;
        return latencies.get(Math.max(index, 0));
    }
    public String getErrorRate() {
        long total = metrics.getTotalRequests();
        if (total == 0) return "0.00%";

        double rate = ((double) metrics.getFailureCount() / total) * 100;
        return String.format("%.2f%%", rate);
    }
    /** Returns throughput in requests per second */
    public double getThroughputSeconds(long durationSeconds) {
        if (durationSeconds <= 0) return 0;
        return (double) metrics.getTotalRequests() / durationSeconds;
    }
}
