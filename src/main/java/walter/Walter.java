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

    /**
     * Runs the main program loop, reading commands from the UI and executing them.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                Command command = Parser.parse(fullCommand);
                assert command != null : "Parsed command should not be null";

                String[] inputs = fullCommand.split(" ", 2);
                assert inputs != null : "Inputs array should not be null";

                if (command == Command.BYE) {
                    isExit = true;
                    ui.showBye();
                } else {
                    executeCommand(command, inputs);
                }
            } catch (WalterException e) {
                ui.showError(e.getMessage());
            } catch (DateTimeParseException e) {
                ui.showError("Invalid date format. Apply yourself! Use 'd/M/yyyy HHmm'.");
            } catch (NumberFormatException e) {
                ui.showError("Enter a valid number. Don't make me repeat myself.");
            } catch (IndexOutOfBoundsException e) {
                ui.showError("That task number does not exist. No loose ends.");
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Executes the logic for a specific command and displays the output via the UI.
     *
     * @param command The parsed command.
     * @param inputs The split input arguments.
     * @throws WalterException If execution fails.
     */
    private void executeCommand(Command command, String[] inputs) throws WalterException {
        switch (command) {
        case LIST:
            ui.showMessage("Here are the tasks in your list. I'm the one who handles the schedule:");
            IntStream.range(0, tasks.size())
                    .forEach(i -> ui.showMessage((i + 1) + "." + tasks.get(i)));
            break;
        case MARK:
            processMarkCommand(inputs);
            break;
        case UNMARK:
            processUnmarkCommand(inputs);
            break;
        case DELETE:
            processDeleteCommand(inputs);
            break;
        case TODO:
            processTodoCommand(inputs);
            break;
        case DEADLINE:
            processDeadlineCommand(inputs);
            break;
        case FIND:
            processFindCommand(inputs);
            break;
        case EVENT:
            processEventCommand(inputs);
            break;
        default:
            break;
        }
    }

    private void processMarkCommand(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify which task to mark. Apply yourself.");
        }
        int markIndex = Integer.parseInt(inputs[1]) - 1;
        assert markIndex >= 0 : "Mark index should be non-negative";
        Task tMark = tasks.get(markIndex);
        assert tMark != null : "Retrieved task should not be null";
        tMark.markAsDone();
        storage.save(tasks);
        ui.showMessage("It's handled. I've marked this task as done:");
        ui.showMessage("  " + tMark);
    }

    private void processUnmarkCommand(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify which task to unmark. Don't waste my time.");
        }
        int unmarkIndex = Integer.parseInt(inputs[1]) - 1;
        assert unmarkIndex >= 0 : "Unmark index should be non-negative";
        Task tUnmark = tasks.get(unmarkIndex);
        assert tUnmark != null : "Retrieved task should not be null";
        tUnmark.unmarkAsDone();
        storage.save(tasks);
        ui.showMessage("You're slipping, Jesse. I've marked this task as not done yet:");
        ui.showMessage("  " + tUnmark);
    }

    private void processDeleteCommand(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify which task to delete. No loose ends.");
        }
        int delIndex = Integer.parseInt(inputs[1]) - 1;
        assert delIndex >= 0 : "Delete index should be non-negative";
        Task tDel = tasks.get(delIndex);
        assert tDel != null : "Retrieved task should not be null";
        tasks.delete(delIndex);
        storage.save(tasks);
        ui.showMessage("No loose ends. I've removed this task:");
        ui.showMessage("  " + tDel);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    private void processTodoCommand(String[] inputs) throws WalterException {
        if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
            throw new WalterException("The description of a todo cannot be empty. Jesse, we need to cook!");
        }
        Task todo = new Todo(inputs[1]);
        assert todo != null : "Created todo task should not be null";
        tasks.add(todo);
        storage.save(tasks);
        printAdded(todo);
    }

    private void processDeadlineCommand(String[] inputs) throws WalterException {
        if (inputs.length < 2 || !inputs[1].contains(" /by ")) {
            throw new WalterException("A deadline must include '/by'. Time is money.");
        }
        String[] dParts = inputs[1].split(" /by ");
        assert dParts.length == 2 : "Deadline parts should be split into exactly 2 parts";
        if (dParts[0].trim().isEmpty()) {
            throw new WalterException("The description of a deadline cannot be empty. Apply yourself!");
        }
        Task deadline = new Deadline(dParts[0], dParts[1]);
        assert deadline != null : "Created deadline task should not be null";
        tasks.add(deadline);
        storage.save(tasks);
        printAdded(deadline);
    }

    private void processFindCommand(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify a keyword. I'm not a mind reader.");
        }
        String keyword = inputs[1];
        assert keyword != null : "Search keyword should not be null";
        ArrayList<Task> foundTasks = tasks.find(keyword);
        assert foundTasks != null : "Found tasks list should not be null";
        ui.showMessage("I'll find what you're looking for. Here are the matches:");
        IntStream.range(0, foundTasks.size())
                .forEach(i -> ui.showMessage((i + 1) + "." + foundTasks.get(i)));
    }

    private void processEventCommand(String[] inputs) throws WalterException {
        if (inputs.length < 2 || !inputs[1].contains(" /from ") || !inputs[1].contains(" /to ")) {
            throw new WalterException("An event must include '/from' and '/to'. Precision is key.");
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
    }

    private void printAdded(Task task) {
        ui.showMessage("Jesse, we need to work. I've added this task:");
        ui.showMessage("  " + task);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
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
            return handleResponse(command, inputs);
        } catch (WalterException e) {
            return "You clearly don't know who you're talking to. " + e.getMessage();
        } catch (DateTimeParseException e) {
            return "Invalid date format. Apply yourself! Use 'd/M/yyyy HHmm'.";
        } catch (NumberFormatException e) {
            return "Enter a valid number. Don't make me repeat myself.";
        } catch (IndexOutOfBoundsException e) {
            return "That task number does not exist. No loose ends.";
        }
    }

    /**
     * Dispatches the command to the appropriate response generator.
     *
     * @param command The parsed command.
     * @param inputs The split input arguments.
     * @return The response string.
     * @throws WalterException If generation fails.
     */
    private String handleResponse(Command command, String[] inputs) throws WalterException {
        switch (command) {
        case BYE:
            return "We're done when I say we're done. Stay out of my territory.";
        case LIST:
            return generateListResponse();
        case MARK:
            return generateMarkResponse(inputs);
        case UNMARK:
            return generateUnmarkResponse(inputs);
        case DELETE:
            return generateDeleteResponse(inputs);
        case TODO:
            return generateTodoResponse(inputs);
        case DEADLINE:
            return generateDeadlineResponse(inputs);
        case EVENT:
            return generateEventResponse(inputs);
        case FIND:
            return generateFindResponse(inputs);
        default:
            return "Unknown command. Stay out of my territory.";
        }
    }

    private String generateListResponse() {
        String msg = "Here are the tasks in your list. I'm the one who handles the schedule:\n";
        StringBuilder sb = new StringBuilder(msg);
        IntStream.range(0, tasks.size())
                .peek(i -> {
                    Task task = tasks.get(i);
                    assert task != null : "Task in list should not be null";
                })
                .forEach(i -> sb.append((i + 1) + "." + tasks.get(i) + "\n"));
        String resultList = sb.toString();
        assert resultList != null : "Result string should not be null";
        return resultList;
    }

    private String generateMarkResponse(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify which task to mark. Apply yourself.");
        }
        int markIndex = Integer.parseInt(inputs[1]) - 1;
        assert markIndex >= 0 : "Mark index should be non-negative";
        Task tMark = tasks.get(markIndex);
        assert tMark != null : "Retrieved task should not be null";
        tMark.markAsDone();
        storage.save(tasks);
        return "It's handled. I've marked this task as done:\n  " + tMark;
    }

    private String generateUnmarkResponse(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify which task to unmark. Don't waste my time.");
        }
        int unmarkIndex = Integer.parseInt(inputs[1]) - 1;
        assert unmarkIndex >= 0 : "Unmark index should be non-negative";
        Task tUnmark = tasks.get(unmarkIndex);
        assert tUnmark != null : "Retrieved task should not be null";
        tUnmark.unmarkAsDone();
        storage.save(tasks);
        return "You're slipping, Jesse. I've marked this task as not done yet:\n  " + tUnmark;
    }

    private String generateDeleteResponse(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify which task to delete. No loose ends.");
        }
        int delIndex = Integer.parseInt(inputs[1]) - 1;
        assert delIndex >= 0 : "Delete index should be non-negative";
        Task tDel = tasks.get(delIndex);
        assert tDel != null : "Retrieved task should not be null";
        tasks.delete(delIndex);
        storage.save(tasks);
        return "No loose ends. I've removed this task:\n  " + tDel + "\nNow you have " + tasks.size()
                + " tasks in the list.";
    }

    private String generateTodoResponse(String[] inputs) throws WalterException {
        if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
            throw new WalterException("The description of a todo cannot be empty. Jesse, we need to cook!");
        }
        Task todo = new Todo(inputs[1]);
        assert todo != null : "Created todo task should not be null";
        tasks.add(todo);
        storage.save(tasks);
        return "Jesse, we need to work. I've added this task:\n  " + todo + "\nNow you have " + tasks.size()
                + " tasks in the list.";
    }

    private String generateDeadlineResponse(String[] inputs) throws WalterException {
        if (inputs.length < 2 || !inputs[1].contains(" /by ")) {
            throw new WalterException("A deadline must include '/by'. Time is money.");
        }
        String[] dParts = inputs[1].split(" /by ");
        assert dParts.length == 2 : "Deadline parts should be split into exactly 2 parts";
        if (dParts[0].trim().isEmpty()) {
            throw new WalterException("The description of a deadline cannot be empty. Apply yourself!");
        }
        Task deadline = new Deadline(dParts[0], dParts[1]);
        assert deadline != null : "Created deadline task should not be null";
        tasks.add(deadline);
        storage.save(tasks);
        return "Time is of the essence. I've added this task:\n  " + deadline + "\nNow you have "
                + tasks.size() + " tasks in the list.";
    }

    private String generateEventResponse(String[] inputs) throws WalterException {
        if (inputs.length < 2 || !inputs[1].contains(" /from ") || !inputs[1].contains(" /to ")) {
            throw new WalterException("An event must include '/from' and '/to'. Precision is key.");
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
        return "I've added this event to the schedule:\n  " + event + "\nNow you have " + tasks.size()
                + " tasks in the list.";
    }

    private String generateFindResponse(String[] inputs) throws WalterException {
        if (inputs.length < 2) {
            throw new WalterException("Specify a keyword. I'm not a mind reader.");
        }
        String keyword = inputs[1];
        assert keyword != null : "Search keyword should not be null";
        ArrayList<Task> foundTasks = tasks.find(keyword);
        assert foundTasks != null : "Found tasks list should not be null";
        StringBuilder sbFind = new StringBuilder("I'll find what you're looking for. Here are the matches:\n");
        IntStream.range(0, foundTasks.size())
            .peek(i -> {
                Task foundTask = foundTasks.get(i);
                assert foundTask != null : "Found task should not be null";
            })
            .forEach(i -> sbFind.append((i + 1) + "." + foundTasks.get(i) + "\n"));
        return sbFind.toString();
    }

    /**
     * Starts the Walter chatbot in CLI mode.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new Walter("data/walter.txt").run();
    }
}

