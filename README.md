# Dartifact API Tester (HTTP Stress Court)[Some Classes are empty, will delete or add something in them later]

**Project Type:** Spring Boot Backend Tool  
**Author:** Velostack  
**Status:** Work in Progress

---

## 🚀 Overview

Dartifact API Tester is a **backend tool to evaluate HTTP endpoints**.  
It sends **real concurrent requests** to a target API and judges its reliability like a “court of law.”  

It measures:  
- Total requests sent  
- Success/failure counts  
- Latency metrics (average, p95)  
- Error rate  
- Provides a **verdict**: `STABLE`, `FLAKY`, `ON_THIN_ICE`, `GUILTY`, or `RATE_LIMITED`.

**Input:** JSON request specifying endpoint, method, headers, body, concurrency, and duration.  
**Output:** JSON response with verdict, metrics summary, and commentary.  

---

## ⚡ Features

### Core Features
- High-concurrency HTTP request execution using `ExecutorService`
- Thread-safe in-memory metrics collection
- Rule-based verdict engine
- Supports GET, POST, PUT, PATCH, etc.
- Custom headers and body support
- JSON input/output, no frontend required (v1)

### Safety Features
- Hard caps on concurrency (`maxConcurrency = 100`) and duration (`maxDuration = 60s`)
- Rate-limit awareness: detects `429` responses
- Graceful failure handling for unreachable endpoints

---

## ⚙️ Installation & Setup

1. Clone the repository:

```bash
git clone https://github.com/yourusername/dartifact-api-tester.git
cd dartifact-api-tester
