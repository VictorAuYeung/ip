package walter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {

    @Test
    public void add_newTask_increaseSize() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("read book"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void delete_existingTask_decreaseSize() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("read book"));
        taskList.add(new Todo("return book"));

        taskList.delete(0); // Delete the first one
        assertEquals(1, taskList.size());
        assertEquals("[T][ ] return book", taskList.get(0).toString());
    }

    @Test
    public void get_invalidIndex_throwsException() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("read book"));

        // Java ArrayList throws IndexOutOfBoundsException, not our custom one
        // (Our custom exception logic is usually in the Ui/Walter class)
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.get(10);
        });
    }
}