package org.example;

import java.time.Instant;

public class IncidentParser {
    public static Incident parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Expected incident, got null");
        }

        String[] properties = s.split(";", -1);

        if (properties.length != 4) {
            throw new IllegalArgumentException("Expected exactly 4 fields");
        }

        Instant start = Instant.parse(properties[2]);
        Instant finish = Instant.parse(properties[3]);

        return new Incident(properties[0], properties[1], start, finish);
    }
}
