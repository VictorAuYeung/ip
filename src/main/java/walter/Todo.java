package walter;

/**
 * Represents a basic task without any associated date or time constraints.
 * <p>
 * A {@code Todo} is the simplest form of a {@link Task}, consisting only of
 * a description and a completion status. It does not have a deadline or an
 * event duration.
 */
public class Todo extends Task {

    /**
     * Constructs a {@code Todo} task with the specified description.
     *
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of the task for display to the user.
     * <p>
     * The output includes the type marker "[T]", followed by the standard
     * task string representation (status and description).
     *
     * @return The formatted string representation of the todo task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns a formatted string suitable for storage in a data file.
     * <p>
     * The format follows the pattern: {@code T | [status] | [description]}.
     *
     * @return The data string representation of the todo task.
     */
    @Override
    public String toFileFormat() {
        return "T" + super.toFileFormat();
    }
}