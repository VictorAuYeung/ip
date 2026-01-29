package walter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that needs to be completed by a specific date and time.
 * <p>
 * A {@code Deadline} extends the {@link Task} class by adding a single due date
 * (the "by" time). It handles the parsing of this date from a specific input string
 * and formats it differently for file storage versus user display.
 */
public class Deadline extends Task {

    /** The due date and time of the deadline. */
    protected LocalDateTime by;

    /**
     * Constructs a {@code Deadline} task with a description and a due date.
     * <p>
     * The date string provided must strictly follow the format <b>"d/M/yyyy HHmm"</b>
     * (e.g., "2/12/2019 1800").
     *
     * @param description The description of the deadline task.
     * @param by          The due date string in "d/M/yyyy HHmm" format.
     * @throws java.time.format.DateTimeParseException if the date string does not match the expected format.
     */
    public Deadline(String description, String by) {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        this.by = LocalDateTime.parse(by, formatter);
    }

    /**
     * Returns a formatted string suitable for storage in a data file.
     * <p>
     * The format follows the pattern: {@code D | [status] | [description] | [by]}.
     * The date is stored in the "d/M/yyyy HHmm" format to ensure consistency during reloading.
     *
     * @return The data string representation of the deadline.
     */
    @Override
    public String toFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        return "D" + super.toFileFormat() + " | " + by.format(formatter);
    }

    /**
     * Returns the string representation of the deadline for display to the user.
     * <p>
     * The output includes the type marker "[D]", the task status, description,
     * and the due date formatted nicely (e.g., "MMM dd yyyy h:mm a").
     *
     * @return The formatted string representation of the deadline.
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a", Locale.US);
        return "[D]" + super.toString() + " (by: " + by.format(outputFormatter) + ")";
    }
}