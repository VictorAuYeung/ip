package walter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    @Test
    public void parse_validCommand_returnsEnum() throws Exception {
        assertEquals(Command.BYE, Parser.parse("bye"));
        assertEquals(Command.LIST, Parser.parse("list"));
        assertEquals(Command.TODO, Parser.parse("todo read book"));
    }

    @Test
    public void parse_invalidCommand_throwsException() {
        // This expects the code to throw a WalterException
        assertThrows(WalterException.class, () -> {
            Parser.parse("blah");
        });
    }

    @Test
    public void parse_emptyInput_throwsException() {
        assertThrows(WalterException.class, () -> {
            Parser.parse("");
        });
    }
}