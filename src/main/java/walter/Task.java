package walter;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        assert description != null : "Task description cannot be null";
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        assert !isDone : "Task is already marked as done";
        this.isDone = true;
    }

    public void unmarkAsDone() {
        assert isDone : "Task is already marked as not done";
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    public String toFileFormat() {
        return " | " + (isDone ? "1" : "0") + " | " + description;
    }
}
