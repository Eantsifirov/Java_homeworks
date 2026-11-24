import org.example.*;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    @Test
    void shouldParseValidIncident() {
        Incident incident = IncidentParser.parse("INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");

        assertEquals("INC-1", incident.id());
        assertEquals("billing", incident.service());
        assertEquals(Instant.parse("2024-03-30T22:30:00Z"), incident.startedAt());
        assertEquals(Instant.parse("2024-03-31T01:00:00Z"), incident.resolvedAt());
        assertEquals(Duration.ofMinutes(150), incident.duration());
    }

    @Test
    void shouldRejectEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(";billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z"));
    }

    @Test
    void shouldRejectFinishBeforeStart() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse("11;billing;2024-03-31T01:00:00Z;2024-03-30T22:30:00Z"));
    }

    @Test
    void shouldRejectIncorrectTime() {
        assertThrows(DateTimeParseException.class, () -> IncidentParser.parse("11;billing;2024-20-45T26:65:00Z;2024-03-31T01:00:00Z"));
    }

    @Test
    void shouldGroupByStartDate() {
        Incident incident = new Incident("INC-1", "billing", Instant.parse("2024-03-30T23:30:00Z"), Instant.parse("2024-03-31T00:30:00Z"));

        List<Incident> incidents = List.of(incident);

        Map<LocalDate, List<Incident>> result = IncidentAnalytics.groupByStartDate(incidents, ZoneId.of("Europe/Berlin"));

        assertEquals(List.of(incident), result.get(LocalDate.of(2024, 3, 31)));
    }

    @Test
    void shouldHandleEmptyIncidentList() {
        assertEquals(Duration.ZERO, IncidentAnalytics.totalDowntime(List.of()));

        assertEquals(Duration.ZERO, IncidentAnalytics.averageDowntime(List.of()));

        assertEquals(Optional.empty(), IncidentAnalytics.longestIncident(List.of()));
    }

    @Test
    void shouldCalcTotalAndAvgDowntime() {
        Instant start = Instant.EPOCH;
        Incident incident1 = new Incident("INC-1", "billing", start, start.plusNanos(1));
        Incident incident2 = new Incident("INC-2", "billing", start, start.plusNanos(2));
        Incident incident3 = new Incident("INC-3", "billing", start, start.plusNanos(2));

        List<Incident> incidents = List.of(incident1, incident2, incident3);
        assertEquals(Duration.ofNanos(5), IncidentAnalytics.totalDowntime(incidents));
        assertEquals(Duration.ofNanos(1), IncidentAnalytics.averageDowntime(incidents));
    }

    @Test
    void shouldReturnLongestIncident() {
        Instant start = Instant.EPOCH;
        Incident incident1 = new Incident("INC-1", "billing", start, start.plus(Duration.ofMinutes(45)));
        Incident incident2 = new Incident("INC-2", "billing", start, start.plus(Duration.ofMinutes(80)));
        Incident incident3 = new Incident("INC-3", "billing", start, start.plus(Duration.ofMinutes(70)));

        List<Incident> incidents = List.of(incident1, incident2, incident3);
        assertEquals(Optional.of(incident2), IncidentAnalytics.longestIncident(incidents));
    }

    @Test
    void shouldDeclineDSTGap() {
        MaintenanceScheduler scheduler = new MaintenanceScheduler(Clock.systemUTC());

        assertThrows(DateTimeException.class, () -> scheduler.nextWindow(LocalDate.of(2024, 3, 31), LocalTime.of(2, 30), Duration.ofHours(1), ZoneId.of("Europe/Berlin"), OverlapPolicy.EARLIER));
    }

    @Test
    void shouldPickCorrectOffset() {
        MaintenanceScheduler scheduler = new MaintenanceScheduler(Clock.systemUTC());

        LocalDate date = LocalDate.of(2024, 10, 27);
        LocalTime time = LocalTime.of(2, 30);
        Duration duration = Duration.ofHours(1);
        ZoneId zone = ZoneId.of("Europe/Berlin");

        MaintenanceScheduler.MaintenanceWindow earlier = scheduler.nextWindow(date, time, duration, zone, OverlapPolicy.EARLIER);
        MaintenanceScheduler.MaintenanceWindow later = scheduler.nextWindow(date, time, duration, zone, OverlapPolicy.LATER);

        assertEquals(ZoneOffset.ofHours(2), earlier.start().atZone(zone).getOffset());
        assertEquals(ZoneOffset.ofHours(1), later.start().atZone(zone).getOffset());
        assertEquals(Duration.ofHours(1), Duration.between(earlier.start(), later.start()));
    }

    @Test
    void shouldCreateUpcomingWindows() {
        Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);

        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        ZoneId zone = ZoneId.of("Europe/Berlin");
        LocalTime time = LocalTime.of(3, 30);
        Duration duration = Duration.ofMinutes(30);

        List<MaintenanceScheduler.MaintenanceWindow> windows = scheduler.upcomingWindows(2, time, duration, zone, OverlapPolicy.EARLIER);

        assertEquals(2, windows.size());
        assertEquals(time, windows.get(0).start().atZone(zone).toLocalTime());
        assertEquals(time, windows.get(1).start().atZone(zone).toLocalTime());
        assertEquals(Duration.ofHours(23), Duration.between(windows.get(0).start(), windows.get(1).start()));
        assertEquals(windows, scheduler.upcomingWindows(2, time, duration, zone, OverlapPolicy.EARLIER));
    }

    @Test
    void shouldFormatReportProperly() {
        MaintenanceScheduler.MaintenanceWindow window = new MaintenanceScheduler.MaintenanceWindow(Instant.parse("2024-03-31T01:00:00Z"), Instant.parse("2024-03-31T02:00:00Z"));

        String result = ReportFormatter.formatWindow(window, ZoneId.of("Europe/Berlin"), Locale.GERMANY);
        assertEquals("31.03.2024 03:00 +02:00 (Europe/Berlin) – 31.03.2024 04:00 +02:00 (Europe/Berlin)", result);
    }

    @Test
    void shouldPreserveInstant() {
        Instant original = Instant.parse("2024-03-31T01:00:00Z");

        Date date = LegacyDateAdapter.toDate(original);
        Instant restored = LegacyDateAdapter.toInstant(date);

        assertEquals(original, restored);
    }
}
