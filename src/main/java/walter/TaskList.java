package walter;

import java.util.ArrayList;

/**
 * Represents a collection of tasks.
 * <p>
 * This class encapsulates an {@code ArrayList} of {@link Task} objects and provides
 * methods to manipulate the list, such as adding, deleting, and retrieving tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty {@code TaskList}.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert this.tasks != null : "Tasks list should be initialized";
    }

    /**
     * Constructs a {@code TaskList} initialized with a pre-existing list of tasks.
     *
     * @param tasks An {@code ArrayList} of {@link Task} objects to initialize the list with.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Provided tasks list cannot be null";
        this.tasks = tasks;
    }

    /**
     * Adds a new task to the list.
     *
     * @param task The {@link Task} object to be added.
     */
    public void add(Task task) {
        assert task != null : "Task to be added cannot be null";
        tasks.add(task);
    }

    /**
     * Removes the task at the specified position in the list.
     *
     * @param index The zero-based index of the task to be removed.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public void delete(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        tasks.remove(index);
    }

    /**
     * Retrieves the task at the specified position in the list.
     *
     * @param index The zero-based index of the task to return.
     * @return The {@link Task} at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";
        return task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves the underlying list of all tasks.
     *
     * @return The {@code ArrayList} containing all tasks.
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> find(String keyword) {
        assert keyword != null : "Search keyword cannot be null";
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            assert task != null : "Task in list should not be null";
            if (task.toString().contains(keyword)) {
                result.add(task);
            }
        }
        assert result != null : "Result list should not be null";
        return result;
    }
}