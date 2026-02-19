package walter;

/**
 * Parses user input into commands.
 */
public class Parser {
    /**
     * Parses the full command string into a {@code Command} enum.
     *
     * @param fullCommand The full user input.
     * @return The corresponding {@code Command}.
     * @throws WalterException If the command is unknown.
     */
    public static Command parse(String fullCommand) throws WalterException {
        assert fullCommand != null : "Full command cannot be null";
        String[] inputs = fullCommand.split(" ", 2);
        assert inputs.length > 0 : "Inputs array should have at least one element";
        String commandString = inputs[0].toUpperCase();

        try {
            return Command.valueOf(commandString);
        } catch (IllegalArgumentException e) {
            throw new WalterException("Unknown command. Stay out of my territory.");
        }
    }
}
