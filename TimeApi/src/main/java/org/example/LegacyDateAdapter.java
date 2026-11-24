package org.example;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

public class LegacyDateAdapter {
    public static Instant toInstant(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Got null as an argument");
        }

        return date.toInstant();
    }

    public static Date toDate(Instant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("Got null as an argument");
        }

        return Date.from(instant);
    }

    public static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Got null as an argument");
        }

        return calendar.toZonedDateTime();
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime zoned) {
        if (zoned == null) {
            throw new IllegalArgumentException("Got null as an argument");
        }

        return GregorianCalendar.from(zoned);
    }
}
