package org.example;

import java.time.Duration;
import java.time.Instant;

public record Incident(String id, String service, Instant startedAt, Instant resolvedAt) {
    public Incident(String id, String service, Instant startedAt, Instant resolvedAt) {
        if (id == null || service == null || startedAt == null || resolvedAt == null) {
            throw new IllegalArgumentException("The arguments aren't nullable");
        }

        if (id.isBlank() || service.isBlank()) {
            throw new IllegalArgumentException("(Id | Service) is empty or consists of spaces");
        }

        if (resolvedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("Resolution time precedes starting time");
        }

        this.id = id;
        this.service = service;
        this.startedAt = startedAt;
        this.resolvedAt = resolvedAt;
    }

    public Duration duration() {
        return Duration.between(startedAt(), resolvedAt());
    }
}
