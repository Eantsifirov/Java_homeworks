package org.example;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class IncidentAnalytics {
    public static Duration totalDowntime(List<Incident> incidents) {
        if (incidents == null) {
            throw new IllegalArgumentException("Incidents list is not nullable");
        }

        Duration sum = Duration.ZERO;

        for (Incident incident : incidents) {
            sum = sum.plus(incident.duration());
        }

        return sum;
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        if (incidents == null) {
            throw new IllegalArgumentException("Incidents list is not nullable");
        }

        if (incidents.isEmpty()) {
            return Duration.ZERO;
        }

        BigInteger total = BigInteger.ZERO;
        BigInteger billion = BigInteger.valueOf(1_000_000_000L);

        for (Incident incident : incidents) {
            Duration d = incident.duration();

            total = total.add(BigInteger.valueOf(d.getSeconds()).multiply(billion).add(BigInteger.valueOf(d.getNano())));
        }

        BigInteger average = total.divide(BigInteger.valueOf(incidents.size()));

        BigInteger[] parts = average.divideAndRemainder(BigInteger.valueOf(1_000_000_000L));

        return Duration.ofSeconds(parts[0].longValueExact(), parts[1].longValueExact());
    }

    public static Optional<Incident> longestIncident(List<Incident> incidents) {
        if (incidents == null) {
            throw new IllegalArgumentException("Incidents list is not nullable");
        }

        for (Incident el : incidents) {
            if (el == null) {
                throw new IllegalArgumentException("Got null as element");
            }
        }

        return incidents.stream().max((a, b) -> {
            int comparison1 = a.duration().compareTo(b.duration());

            if (comparison1 != 0) {
                return comparison1;
            }

            int comparison2 = b.startedAt().compareTo(a.startedAt());

            if (comparison2 != 0) {
                return comparison2;
            }

            return b.id().compareTo(a.id());
        });
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(List<Incident> incidents, ZoneId zone) {
        if (incidents == null || zone == null) {
            throw new IllegalArgumentException("Got null as an argument");
        }

        Map<LocalDate, List<Incident>> result = new TreeMap<>();

        for (Incident incident : incidents) {
            if (incident == null) {
                throw new IllegalArgumentException("Got null as an incident");
            }

            LocalDate date = incident.startedAt().atZone(zone).toLocalDate();

            if (!result.containsKey(date)) {
                result.put(date, new ArrayList<>());
            }

            result.get(date).add(incident);
        }

        for (List<Incident> group : result.values()) {
            group.sort((a, b) -> {
                int comparison = a.startedAt().compareTo(b.startedAt());

                if (comparison != 0) {
                    return comparison;
                }

                return a.id().compareTo(b.id());
            });
        }

        return result;
    }
}
