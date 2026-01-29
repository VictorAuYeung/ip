package walter;

import org.junit.jupiter.api.Test;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineTest {

    @Test
    public void toString_validDate_formattedCorrectly() {
        // "2/12/2019 1800" should become "Dec 02 2019 6:00 PM"
        Deadline deadline = new Deadline("return book", "2/12/2019 1800");
        assertEquals("[D][ ] return book (by: Dec 02 2019 6:00 PM)", deadline.toString());
    }

    @Test
    public void toFileFormat_validDate_storesOriginalFormat() {
        // Should store as "2/12/2019 1800" so it can be re-parsed on load
        Deadline deadline = new Deadline("return book", "2/12/2019 1800");
        assertEquals("D | 0 | return book | 2/12/2019 1800", deadline.toFileFormat());
    }

    @Test
    public void constructor_invalidDate_throwsException() {
        assertThrows(DateTimeParseException.class, () -> {
            new Deadline("return book", "invalid-date-format");
        });
    }
}