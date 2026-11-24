package org.example;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public final class MaintenanceScheduler {
    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        if (clock == null) {
            throw new IllegalArgumentException("Got null as a clock");
        }

        this.clock = clock;
    }

    public record MaintenanceWindow(Instant start, Instant end) {
        public MaintenanceWindow(Instant start, Instant end) {
            if (start == null || end == null) {
                throw new IllegalArgumentException("Got null as an argument");
            }

            if (end.isBefore(start)) {
                throw new IllegalArgumentException("End precedes start");
            }

            this.start = start;
            this.end = end;
        }

        public Duration duration() {
            return Duration.between(start, end);
        }
    }

    public MaintenanceWindow nextWindow(LocalDate date, LocalTime localStart, Duration duration, ZoneId zone, OverlapPolicy policy) {
        if (date == null || localStart == null || duration == null || zone == null || policy == null) {
            throw new IllegalArgumentException("Got null as an argument");
        }

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Non-positive duration");
        }

        LocalDateTime local = LocalDateTime.of(date, localStart);
        List<ZoneOffset> offsets = zone.getRules().getValidOffsets(local);

        if (offsets.isEmpty()) {
            throw new DateTimeException("This time does not exist locally");
        }

        ZonedDateTime zoned = local.atZone(zone);

        if (policy == OverlapPolicy.EARLIER) {
            zoned = zoned.withEarlierOffsetAtOverlap();
        } else if (policy == OverlapPolicy.LATER) {
            zoned = zoned.withLaterOffsetAtOverlap();
        }

        Instant start = zoned.toInstant();
        Instant end = start.plus(duration);

        return new MaintenanceWindow(start, end);
    }

    public List<MaintenanceWindow> upcomingWindows(int days, LocalTime localStart, Duration duration, ZoneId zone, OverlapPolicy policy) {
        if (localStart == null || duration == null || zone == null || policy == null) {
            throw new IllegalArgumentException("Got null as an argument");
        }

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Non-positive duration");
        }

        if (days < 0) {
            throw new IllegalArgumentException("Got negative days");
        }

        LocalDate date = LocalDate.now(clock.withZone(zone));
        List<MaintenanceWindow> windows = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            windows.add(nextWindow(date, localStart, duration, zone, policy));
            date = date.plusDays(1);
        }

        return windows;
    }
}
