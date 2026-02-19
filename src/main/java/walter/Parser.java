package walter;

public class Parser {
    public static Command parse(String fullCommand) throws WalterException {
        assert fullCommand != null : "Full command cannot be null";
        assert !fullCommand.trim().isEmpty() : "Command cannot be empty";
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