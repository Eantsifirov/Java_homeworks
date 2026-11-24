package org.example;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReportFormatter {
    public static String formatWindow(MaintenanceScheduler.MaintenanceWindow window, ZoneId zone, Locale locale) {
        if (window == null || zone == null || locale == null) {
            throw new IllegalArgumentException("Got null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm XXX (VV)", locale);
        ZonedDateTime zonedStart = window.start().atZone(zone);
        String formattedStart = zonedStart.format(formatter);

        ZonedDateTime zonedEnd = window.end().atZone(zone);
        String  formattedEnd = zonedEnd.format(formatter);

        return formattedStart + " – " + formattedEnd;
    }
}
