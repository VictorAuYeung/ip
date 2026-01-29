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
        System.out.println("     Hello! I'm Walter\n     What can I do for you?");
        showLine();
    }

    /**
     * Displays the farewell message when the user exits the application.
     */
    public void showBye() {
        System.out.println("     Bye. Hope to see you again soon!");
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
        System.out.println("     OOPS!!! " + message);
    }

    /**
     * Displays a specific error message indicating that the storage file could not be loaded.
     */
    public void showLoadingError() {
        System.out.println("     OOPS!!! Error loading file. Starting with empty list.");
    }

    /**
     * Prints a generic message to the user with standard indentation.
     *
     * @param message The message content to be displayed.
     */
    public void showMessage(String message) {
        System.out.println("     " + message);
    }
}