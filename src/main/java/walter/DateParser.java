package walter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;

/**
 * Utility class to parse date-time strings.
 * Supports both strict format (d/M/yyyy HHmm) and natural language (e.g., today, tomorrow, Mon).
 */
public class DateParser {
    private static final DateTimeFormatter STRICT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    /**
     * Parses a date-time string.
     *
     * @param input The input string to parse.
     * @return The parsed LocalDateTime.
     * @throws DateTimeParseException If the input cannot be parsed.
     */
    public static LocalDateTime parse(String input) throws DateTimeParseException {
        try {
            return LocalDateTime.parse(input, STRICT_FORMATTER);
        } catch (DateTimeParseException e) {
            return parseNatural(input);
        }
    }

    private static LocalDateTime parseNatural(String input) throws DateTimeParseException {
        String[] parts = input.trim().split("\\s+");
        if (parts.length == 0) {
            throw new DateTimeParseException("Empty date string", input, 0);
        }

        String datePart = parts[0].toLowerCase();
        String timePart = parts.length > 1 ? parts[1] : "2359";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetDate = null;

        if (datePart.equals("today")) {
            targetDate = now;
        } else if (datePart.equals("tomorrow")) {
            targetDate = now.plusDays(1);
        } else {
            DayOfWeek targetDay = getDayOfWeek(datePart);
            if (targetDay != null) {
                targetDate = now.with(TemporalAdjusters.next(targetDay));
            }
        }

        if (targetDate != null) {
            try {
                LocalTime time = parseTime(timePart);
                return targetDate.with(time).withNano(0);
            } catch (DateTimeParseException e) {
                // If it's just "Mon" or "today", and the second part isn't a valid time,
                // maybe the second part was something else? But usually we expect time.
                // If we only have one part, we already defaulted timePart to "2359".
                if (parts.length == 1) {
                    return targetDate.with(LocalTime.of(23, 59)).withNano(0);
                }
            }
        }

        throw new DateTimeParseException("Could not parse natural date: " + input, input, 0);
    }

    private static LocalTime parseTime(String timeStr) {
        if (timeStr.length() == 4) {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HHmm"));
        }
        return LocalTime.parse(timeStr); // Try ISO format
    }

    private static DayOfWeek getDayOfWeek(String input) {
        if (input.startsWith("mon")) {
            return DayOfWeek.MONDAY;
        }
        if (input.startsWith("tue")) {
            return DayOfWeek.TUESDAY;
        }
        if (input.startsWith("wed")) {
            return DayOfWeek.WEDNESDAY;
        }
        if (input.startsWith("thu")) {
            return DayOfWeek.THURSDAY;
        }
        if (input.startsWith("fri")) {
            return DayOfWeek.FRIDAY;
        }
        if (input.startsWith("sat")) {
            return DayOfWeek.SATURDAY;
        }
        if (input.startsWith("sun")) {
            return DayOfWeek.SUNDAY;
        }
        return null;
    }
}
