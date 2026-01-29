package walter;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * The main entry point for the Walter chatbot.
 * <p>
 * This class initializes the application components ({@link Ui}, {@link Storage},
 * and {@link TaskList}) and contains the main program loop. It coordinates the
 * interaction between the user, the data logic, and the file storage system.
 */
public class Walter {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;


    /**
     * Constructs a new {@code Walter} application instance.
     * <p>
     * Initializes the UI and Storage components. It attempts to load existing
     * tasks from the specified file path. If loading fails (e.g., file not found
     * or corrupted), it initializes an empty task list and displays an error to the user.
     *
     * @param filePath The relative path to the file where task data is stored.
     */
    public Walter(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (WalterException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                // Parse the Command Type using the Parser class
                Command command = Parser.parse(fullCommand);

                // split arguments for the logic below
                String[] inputs = fullCommand.split(" ", 2);

                switch (command) {
                    case BYE:
                        isExit = true;
                        ui.showBye();
                        break;

                    case LIST:
                        ui.showMessage("Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            ui.showMessage((i + 1) + "." + tasks.get(i));
                        }
                        break;

                    case MARK:
                        if (inputs.length < 2) throw new WalterException("Please specify which task to mark.");
                        int markIndex = Integer.parseInt(inputs[1]) - 1;
                        Task tMark = tasks.get(markIndex);
                        tMark.markAsDone();
                        storage.save(tasks);
                        ui.showMessage("Nice! I've marked this task as done:");
                        ui.showMessage("  " + tMark);
                        break;

                    case UNMARK:
                        if (inputs.length < 2) throw new WalterException("Please specify which task to unmark.");
                        int unmarkIndex = Integer.parseInt(inputs[1]) - 1;
                        Task tUnmark = tasks.get(unmarkIndex);
                        tUnmark.unmarkAsDone();
                        storage.save(tasks);
                        ui.showMessage("OK, I've marked this task as not done yet:");
                        ui.showMessage("  " + tUnmark);
                        break;

                    case DELETE:
                        if (inputs.length < 2) throw new WalterException("Please specify which task to delete.");
                        int delIndex = Integer.parseInt(inputs[1]) - 1;
                        Task tDel = tasks.get(delIndex);
                        tasks.delete(delIndex);
                        storage.save(tasks);
                        ui.showMessage("Noted. I've removed this task:");
                        ui.showMessage("  " + tDel);
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                        break;

                    case TODO:
                        if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
                            throw new WalterException("The description of a todo cannot be empty.");
                        }
                        Task todo = new Todo(inputs[1]);
                        tasks.add(todo);
                        storage.save(tasks);
                        printAdded(todo);
                        break;

                    case DEADLINE:
                        if (inputs.length < 2 || !inputs[1].contains(" /by ")) {
                            throw new WalterException("A deadline must include '/by' to specify the date.");
                        }
                        String[] dParts = inputs[1].split(" /by ");
                        if (dParts[0].trim().isEmpty()) {
                            throw new WalterException("The description of a deadline cannot be empty.");
                        }
                        Task deadline = new Deadline(dParts[0], dParts[1]);
                        tasks.add(deadline);
                        storage.save(tasks);
                        printAdded(deadline);
                        break;
                    case FIND:
                    if (inputs.length < 2) throw new WalterException("Please specify a keyword to search.");
                    String keyword = inputs[1];
                    ArrayList<Task> foundTasks = tasks.find(keyword); // You need to implement this in TaskList
                    ui.showMessage("Here are the matching tasks in your list:");
                    for (int i = 0; i < foundTasks.size(); i++) {
                        ui.showMessage((i + 1) + "." + foundTasks.get(i));
                    }
                    break;
                    case EVENT:
                        if (inputs.length < 2 || !inputs[1].contains(" /from ") || !inputs[1].contains(" /to ")) {
                            throw new WalterException("An event must include '/from' and '/to' to specify the timing.");
                        }
                        String[] eParts = inputs[1].split(" /from ");
                        String description = eParts[0];
                        String[] timeParts = eParts[1].split(" /to ");
                        Task event = new Event(description, timeParts[0], timeParts[1]);
                        tasks.add(event);
                        storage.save(tasks);
                        printAdded(event);
                        break;
                }
            } catch (WalterException e) {
                ui.showError(e.getMessage());
            } catch (DateTimeParseException e) {
                ui.showError("Invalid date format. Please use 'd/M/yyyy HHmm'.");
                ui.showError("Example: 2/12/2019 1800");
            } catch (NumberFormatException e) {
                ui.showError("Please enter a valid number.");
            } catch (IndexOutOfBoundsException e) {
                ui.showError("That task number does not exist.");
            } finally {
                ui.showLine();
            }
        }
    }

    private void printAdded(Task task) {
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + task);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    public static void main(String[] args) {
        new Walter("data/walter.txt").run();
    }
}