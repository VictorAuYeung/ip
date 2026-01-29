package walter;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcome() {
        showLine();
        System.out.println("     Hello! I'm Walter\n     What can I do for you?");
        showLine();
    }

    public void showBye() {
        System.out.println("     Bye. Hope to see you again soon!");
    }

    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    public void showError(String message) {
        System.out.println("     OOPS!!! " + message);
    }

    public void showLoadingError() {
        System.out.println("     OOPS!!! Error loading file. Starting with empty list.");
    }

    public void showMessage(String message) {
        System.out.println("     " + message);
    }
}