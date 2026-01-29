package walter;  // Same package as main code

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {

    @Test
    public void toString_newTodo_formattedCorrectly() {
        // Test that a new Todo prints correctly
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void toFileFormat_newTodo_formattedCorrectly() {
        // Test that a new Todo saves correctly
        Todo todo = new Todo("read book");
        assertEquals("T | 0 | read book", todo.toFileFormat());
    }

    @Test
    public void markAsDone_todo_updatesStatus() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
    }
}