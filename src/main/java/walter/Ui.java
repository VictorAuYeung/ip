package walter;

import java.util.Scanner;

/**
 * Handles all interactions with the user.
 * <p>
 * This class encapsulates the details of reading input from the standard input
 * and printing formatted messages to the standard output. It ensures consistent
 * visual presentation (indentation and divider lines) across the application.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a new {@code Ui} instance and initializes the input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
        assert this.scanner != null : "Scanner should be initialized";
    }

    /**
     * Reads the next line of input from the user.
     *
     * @return The full command string entered by the user, trimmed of leading/trailing whitespace.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome message and logo to the user upon application startup.
     */
    public void showWelcome() {
        showLine();
        System.out.println("     I am the one who knocks! I am Walter White.\n     What do you need? Apply yourself.");
        showLine();
    }

    /**
     * Displays the farewell message when the user exits the application.
     */
    public void showBye() {
        System.out.println("     We're done when I say we're done. Stay out of my territory.");
    }

    /**
     * Prints a horizontal divider line to visually separate command outputs.
     */
    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    /**
     * Displays an error message to the user with a standard prefix.
     * <p>
     * The message is prefixed with "OOPS!!!" to indicate an exception or invalid input.
     *
     * @param message The specific error message to display.
     */
    public void showError(String message) {
        System.out.println("     You clearly don't know who you're talking to. " + message);
    }

    /**
     * Displays a specific error message indicating that the storage file could not be loaded.
     */
    public void showLoadingError() {
        System.out.println("     Error loading file. I'll start with an empty list for now.");
    }

    /**
     * Prints a generic message to the user with standard indentation.
     *
     * @param message The message content to be displayed.
     */
    public void showMessage(String message) {
        assert message != null : "Message to display cannot be null";
        System.out.println("     " + message);
    }
}