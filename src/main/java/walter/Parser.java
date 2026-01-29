package walter;

public class Parser {
    public static Command parse(String fullCommand) throws WalterException {
        String[] inputs = fullCommand.split(" ", 2);
        String commandString = inputs[0].toUpperCase();

        try {
            return Command.valueOf(commandString);
        } catch (IllegalArgumentException e) {
            throw new WalterException("I'm sorry, but I don't know what that means :-(");
        }
    }
}