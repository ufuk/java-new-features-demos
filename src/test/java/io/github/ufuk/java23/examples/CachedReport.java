package io.github.ufuk.java23.examples;

import java.time.Instant;
import java.util.UUID;

public class CachedReport extends BaseReport {

    private final String reportId;
    private final Instant generatedAt;

    public CachedReport(String title) {
        // Initializing fields of the subclass BEFORE super(...) (Flexible Constructor Bodies - JEP 482 preview in Java 23)
        String id = UUID.randomUUID().toString();
        this.reportId = id;
        this.generatedAt = Instant.now();

        super("Report: " + title + " [ID: " + id + "]");
    }

    public String getReportId() {
        return reportId;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

}
