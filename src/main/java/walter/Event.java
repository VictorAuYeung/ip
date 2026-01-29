package walter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that occurs during a specific time period.
 * <p>
 * An {@code Event} extends the {@link Task} class by adding a start time
 * and an end time. It handles date-time parsing and formatting for both
 * file storage and user display.
 */
public class Event extends Task {

    /** The start time of the event. */
    protected LocalDateTime from;

    /** The end time of the event. */
    protected LocalDateTime to;

    /**
     * Constructs an {@code Event} task with a description, start time, and end time.
     * <p>
     * The date strings provided must strictly follow the format <b>"d/M/yyyy HHmm"</b>
     * (e.g., "2/12/2019 1800").
     *
     * @param description The description of the event task.
     * @param from        The start time string in "d/M/yyyy HHmm" format.
     * @param to          The end time string in "d/M/yyyy HHmm" format.
     * @throws java.time.format.DateTimeParseException if the date strings do not match the expected format.
     */
    public Event(String description, String from, String to) {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        this.from = LocalDateTime.parse(from, formatter);
        this.to = LocalDateTime.parse(to, formatter);
    }

    /**
     * Returns a formatted string suitable for storage in a data file.
     * <p>
     * The format follows the pattern: {@code E | [status] | [description] | [from] | [to]}.
     * The dates are stored in the "d/M/yyyy HHmm" format to ensure consistency during reloading.
     *
     * @return The data string representation of the event.
     */
    @Override
    public String toFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        return "E" + super.toFileFormat() + " | " + from.format(formatter) + " | " + to.format(formatter);
    }

    /**
     * Returns the string representation of the event for display to the user.
     * <p>
     * The output includes the type marker "[E]", the task status, description,
     * and the time interval formatted nicely (e.g., "MMM dd yyyy h:mm a").
     *
     * @return The formatted string representation of the event.
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a", Locale.US);
        return "[E]" + super.toString() + " (from: " + from.format(outputFormatter) +
                " to: " + to.format(outputFormatter) + ")";
    }
}