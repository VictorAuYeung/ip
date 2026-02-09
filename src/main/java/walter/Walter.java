package walter;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.IntStream;

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
        assert filePath != null : "File path cannot be null";
        ui = new Ui();
        assert ui != null : "Ui should be initialized";
        storage = new Storage(filePath);
        assert storage != null : "Storage should be initialized";
        try {
            tasks = new TaskList(storage.load());
        } catch (WalterException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
        assert tasks != null : "Tasks should be initialized";
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
                assert command != null : "Parsed command should not be null";

                // split arguments for the logic below
                String[] inputs = fullCommand.split(" ", 2);
                assert inputs != null : "Inputs array should not be null";

                switch (command) {
                case BYE:
                    isExit = true;
                    ui.showBye();
                    break;

                case LIST:
                    ui.showMessage("Here are the tasks in your list:");
                    IntStream.range(0, tasks.size())
                            .forEach(i -> ui.showMessage((i + 1) + "." + tasks.get(i)));
                    break;

                case MARK:
                    if (inputs.length < 2) {
                        throw new WalterException("Please specify which task to mark.");
                    }
                    int markIndex = Integer.parseInt(inputs[1]) - 1;
                    assert markIndex >= 0 : "Mark index should be non-negative";
                    Task tMark = tasks.get(markIndex);
                    assert tMark != null : "Retrieved task should not be null";
                    tMark.markAsDone();
                    storage.save(tasks);
                    ui.showMessage("Nice! I've marked this task as done:");
                    ui.showMessage("  " + tMark);
                    break;

                case UNMARK:
                    if (inputs.length < 2) {
                        throw new WalterException("Please specify which task to unmark.");
                    }
                    int unmarkIndex = Integer.parseInt(inputs[1]) - 1;
                    assert unmarkIndex >= 0 : "Unmark index should be non-negative";
                    Task tUnmark = tasks.get(unmarkIndex);
                    assert tUnmark != null : "Retrieved task should not be null";
                    tUnmark.unmarkAsDone();
                    storage.save(tasks);
                    ui.showMessage("OK, I've marked this task as not done yet:");
                    ui.showMessage("  " + tUnmark);
                    break;

                case DELETE:
                    if (inputs.length < 2) {
                        throw new WalterException("Please specify which task to delete.");
                    }
                    int delIndex = Integer.parseInt(inputs[1]) - 1;
                    assert delIndex >= 0 : "Delete index should be non-negative";
                    Task tDel = tasks.get(delIndex);
                    assert tDel != null : "Retrieved task should not be null";
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
                    assert todo != null : "Created todo task should not be null";
                    tasks.add(todo);
                    storage.save(tasks);
                    printAdded(todo);
                    break;

                case DEADLINE:
                    if (inputs.length < 2 || !inputs[1].contains(" /by ")) {
                        throw new WalterException("A deadline must include '/by' to specify the date.");
                    }
                    String[] dParts = inputs[1].split(" /by ");
                    assert dParts.length == 2 : "Deadline parts should be split into exactly 2 parts";
                    if (dParts[0].trim().isEmpty()) {
                        throw new WalterException("The description of a deadline cannot be empty.");
                    }
                    Task deadline = new Deadline(dParts[0], dParts[1]);
                    assert deadline != null : "Created deadline task should not be null";
                    tasks.add(deadline);
                    storage.save(tasks);
                    printAdded(deadline);
                    break;
                case FIND:
                    if (inputs.length < 2) {
                        throw new WalterException("Please specify a keyword to search.");
                    }
                    String keyword = inputs[1];
                    assert keyword != null : "Search keyword should not be null";
                    ArrayList<Task> foundTasks = tasks.find(keyword);
                    assert foundTasks != null : "Found tasks list should not be null"; // You need to implement this in TaskList
                    ui.showMessage("Here are the matching tasks in your list:");
                    IntStream.range(0, foundTasks.size())
                            .forEach(i -> ui.showMessage((i + 1) + "." + foundTasks.get(i)));
                    break;
                case EVENT:
                    if (inputs.length < 2 || !inputs[1].contains(" /from ") || !inputs[1].contains(" /to ")) {
                        throw new WalterException("An event must include '/from' and '/to' to specify the timing.");
                    }
                    String[] eParts = inputs[1].split(" /from ");
                    assert eParts.length == 2 : "Event description and time should be split into 2 parts";
                    String description = eParts[0];
                    String[] timeParts = eParts[1].split(" /to ");
                    assert timeParts.length == 2 : "Event start and end time should be split into 2 parts";
                    Task event = new Event(description, timeParts[0], timeParts[1]);
                    assert event != null : "Created event task should not be null";
                    tasks.add(event);
                    storage.save(tasks);
                    printAdded(event);
                    break;
                default:
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

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user command string.
     * @return The response string from the bot.
     */
    public String getResponse(String input) {
        assert input != null : "Input command cannot be null";
        assert !input.trim().isEmpty() : "Input command cannot be empty";
        try {
            Command command = Parser.parse(input);
            assert command != null : "Parsed command should not be null";
            String[] inputs = input.split(" ", 2);
            assert inputs != null : "Inputs array should not be null";
            switch (command) {
            case BYE:
                return "Bye. Hope to see you again soon!";
            case LIST:
                StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
                IntStream.range(0, tasks.size())
                        .peek(i -> {
                            Task task = tasks.get(i);
                            assert task != null : "Task in list should not be null";
                        })
                        .forEach(i -> sb.append((i + 1) + "." + tasks.get(i) + "\n"));
                String resultList = sb.toString();
                assert resultList != null : "Result string should not be null";
                return resultList;
            case MARK:
                if (inputs.length < 2) {
                    throw new WalterException("Please specify which task to mark.");
                }
                int markIndex = Integer.parseInt(inputs[1]) - 1;
                assert markIndex >= 0 : "Mark index should be non-negative";
                Task tMark = tasks.get(markIndex);
                assert tMark != null : "Retrieved task should not be null";
                tMark.markAsDone();
                storage.save(tasks);
                String markResult = "Nice! I've marked this task as done:\n  " + tMark;
                assert markResult != null : "Result string should not be null";
                return markResult;
            case UNMARK:
                if (inputs.length < 2) {
                    throw new WalterException("Please specify which task to unmark.");
                }
                int unmarkIndex = Integer.parseInt(inputs[1]) - 1;
                assert unmarkIndex >= 0 : "Unmark index should be non-negative";
                Task tUnmark = tasks.get(unmarkIndex);
                assert tUnmark != null : "Retrieved task should not be null";
                tUnmark.unmarkAsDone();
                storage.save(tasks);
                String unmarkResult = "OK, I've marked this task as not done yet:\n  " + tUnmark;
                assert unmarkResult != null : "Result string should not be null";
                return unmarkResult;
            case DELETE:
                if (inputs.length < 2) {
                    throw new WalterException("Please specify which task to delete.");
                }
                int delIndex = Integer.parseInt(inputs[1]) - 1;
                assert delIndex >= 0 : "Delete index should be non-negative";
                Task tDel = tasks.get(delIndex);
                assert tDel != null : "Retrieved task should not be null";
                tasks.delete(delIndex);
                storage.save(tasks);
                String delResult = "Noted. I've removed this task:\n  " + tDel + "\nNow you have " + tasks.size()
                        + " tasks in the list.";
                assert delResult != null : "Result string should not be null";
                return delResult;
            case TODO:
                if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
                    throw new WalterException("The description of a todo cannot be empty.");
                }
                Task todo = new Todo(inputs[1]);
                assert todo != null : "Created todo task should not be null";
                tasks.add(todo);
                storage.save(tasks);
                String todoResult = "Got it. I've added this task:\n  " + todo + "\nNow you have " + tasks.size()
                        + " tasks in the list.";
                assert todoResult != null : "Result string should not be null";
                return todoResult;
            case DEADLINE:
                if (inputs.length < 2 || !inputs[1].contains(" /by ")) {
                    throw new WalterException("A deadline must include '/by' to specify the date.");
                }
                String[] dParts = inputs[1].split(" /by ");
                assert dParts.length == 2 : "Deadline parts should be split into exactly 2 parts";
                if (dParts[0].trim().isEmpty()) {
                    throw new WalterException("The description of a deadline cannot be empty.");
                }
                Task deadline = new Deadline(dParts[0], dParts[1]);
                assert deadline != null : "Created deadline task should not be null";
                tasks.add(deadline);
                storage.save(tasks);
                String deadlineResult = "Got it. I've added this task:\n  " + deadline + "\nNow you have "
                        + tasks.size() + " tasks in the list.";
                assert deadlineResult != null : "Result string should not be null";
                return deadlineResult;
            case EVENT:
                if (inputs.length < 2 || !inputs[1].contains(" /from ") || !inputs[1].contains(" /to ")) {
                    throw new WalterException("An event must include '/from' and '/to' to specify the timing.");
                }
                String[] eParts = inputs[1].split(" /from ");
                assert eParts.length == 2 : "Event description and time should be split into 2 parts";
                String description = eParts[0];
                String[] timeParts = eParts[1].split(" /to ");
                assert timeParts.length == 2 : "Event start and end time should be split into 2 parts";
                Task event = new Event(description, timeParts[0], timeParts[1]);
                assert event != null : "Created event task should not be null";
                tasks.add(event);
                storage.save(tasks);
                String eventResult = "Got it. I've added this task:\n  " + event + "\nNow you have " + tasks.size()
                        + " tasks in the list.";
                assert eventResult != null : "Result string should not be null";
                return eventResult;
            case FIND:
                if (inputs.length < 2) {
                    throw new WalterException("Please specify a keyword to search.");
                }
                String keyword = inputs[1];
                assert keyword != null : "Search keyword should not be null";
                ArrayList<Task> foundTasks = tasks.find(keyword);
                assert foundTasks != null : "Found tasks list should not be null";
                StringBuilder sbFind = new StringBuilder("Here are the matching tasks in your list:\n");
                IntStream.range(0, foundTasks.size())
                    .peek(i -> {
                        Task foundTask = foundTasks.get(i);
                        assert foundTask != null : "Found task should not be null";
                    })
                    .forEach(i -> sbFind.append((i + 1) + "." + foundTasks.get(i) + "\n"));
                String findResult = sbFind.toString();
                assert findResult != null : "Result string should not be null";
                return findResult;
            default:
                return "Unknown command.";
            }
        } catch (WalterException e) {
            return "OOPS!!! " + e.getMessage();
        } catch (DateTimeParseException e) {
            return "OOPS!!! Invalid date format. Please use 'd/M/yyyy HHmm'.\nExample: 2/12/2019 1800";
        } catch (NumberFormatException e) {
            return "OOPS!!! Please enter a valid number.";
        } catch (IndexOutOfBoundsException e) {
            return "OOPS!!! That task number does not exist.";
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
