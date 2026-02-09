package walter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.Test;

public class DateParserTest {

    @Test
    public void parse_strictFormat_success() {
        String input = "2/12/2019 1800";
        LocalDateTime expected = LocalDateTime.of(2019, 12, 2, 18, 0);
        assertEquals(expected, DateParser.parse(input));
    }

    @Test
    public void parse_today_success() {
        LocalDateTime result = DateParser.parse("today");
        assertNotNull(result);
        assertEquals(LocalDateTime.now().getDayOfYear(), result.getDayOfYear());
        assertEquals(LocalTime.of(23, 59), result.toLocalTime());
    }

    @Test
    public void parse_tomorrow_success() {
        LocalDateTime result = DateParser.parse("tomorrow");
        assertNotNull(result);
        assertEquals(LocalDateTime.now().plusDays(1).getDayOfYear(), result.getDayOfYear());
        assertEquals(LocalTime.of(23, 59), result.toLocalTime());
    }

    @Test
    public void parse_monday_success() {
        LocalDateTime result = DateParser.parse("Mon");
        assertNotNull(result);
        LocalDateTime expectedDate = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        assertEquals(expectedDate.toLocalDate(), result.toLocalDate());
        assertEquals(LocalTime.of(23, 59), result.toLocalTime());
    }

    @Test
    public void parse_mondayWithTime_success() {
        LocalDateTime result = DateParser.parse("Mon 1800");
        assertNotNull(result);
        LocalDateTime expectedDate = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        assertEquals(expectedDate.toLocalDate(), result.toLocalDate());
        assertEquals(LocalTime.of(18, 0), result.toLocalTime());
    }

    @Test
    public void parse_invalidNatural_throwsException() {
        assertThrows(DateTimeParseException.class, () -> DateParser.parse("sometime"));
    }
}
