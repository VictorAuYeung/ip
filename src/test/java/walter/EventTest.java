package walter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    @Test
    public void toString_validDates_formattedCorrectly() {
        Event event = new Event("project meeting", "2/12/2019 1400", "2/12/2019 1600");
        // Expecting: [E][ ] project meeting (from: Dec 02 2019 2:00 PM to: Dec 02 2019 4:00 PM)
        String expected = "[E][ ] project meeting (from: Dec 02 2019 2:00 PM to: Dec 02 2019 4:00 PM)";
        assertEquals(expected, event.toString());
    }

    @Test
    public void toFileFormat_validDates_storesOriginalFormat() {
        Event event = new Event("project meeting", "2/12/2019 1400", "2/12/2019 1600");
        String expected = "E | 0 | project meeting | 2/12/2019 1400 | 2/12/2019 1600";
        assertEquals(expected, event.toFileFormat());
    }
}