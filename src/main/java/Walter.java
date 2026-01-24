import java.util.Scanner;
import java.util.ArrayList;

public class Walter {
    public static void main(String[] args) {
        String horizontalLine = "    ____________________________________________________________";
        System.out.println(horizontalLine);
        System.out.println("     Hello! I'm Walter\n     What can I do for you?");
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine();
            System.out.println(horizontalLine);

            try {
                String[] inputs = input.split(" ", 2);
                String commandString = inputs[0].toUpperCase();

                Command command;
                try {
                    command = Command.valueOf(commandString);
                } catch (IllegalArgumentException e) {
                    throw new WalterException("I'm sorry, but I don't know what that means AHHH :-(");
                }

                switch (command) {
                    case BYE:
                        System.out.println("     Bye. Hope to see you again soon!");
                        System.out.println(horizontalLine);
                        scanner.close();
                        return;

                    case LIST:
                        System.out.println("     Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println("     " + (i + 1) + "." + tasks.get(i));
                        }
                        break;

                    case MARK:
                        if (inputs.length < 2) throw new WalterException("Please specify which task to mark.");
                        int markIndex = Integer.parseInt(inputs[1]) - 1;
                        Task tMark = tasks.get(markIndex);
                        tMark.markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + tMark);
                        break;

                    case UNMARK:
                        if (inputs.length < 2) throw new WalterException("Please specify which task to unmark.");
                        int unmarkIndex = Integer.parseInt(inputs[1]) - 1;
                        Task tUnmark = tasks.get(unmarkIndex);
                        tUnmark.unmarkAsDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + tUnmark);
                        break;

                    case DELETE:
                        if (inputs.length < 2) throw new WalterException("Please specify which task to delete.");
                        int delIndex = Integer.parseInt(inputs[1]) - 1;
                        Task tDel = tasks.get(delIndex);
                        tasks.remove(delIndex);
                        System.out.println("     Noted. I've removed this task:");
                        System.out.println("       " + tDel);
                        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                        break;

                    case TODO:
                        if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
                            throw new WalterException("The description of a todo cannot be empty.");
                        }
                        Task todo = new Todo(inputs[1]);
                        tasks.add(todo);
                        printAdded(todo, tasks.size());
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
                        printAdded(deadline, tasks.size());
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
                        printAdded(event, tasks.size());
                        break;
                }

            } catch (WalterException e) {
                System.out.println("     OOPS!!! " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("     OOPS!!! Please enter a valid number.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("     OOPS!!! That task number does not exist.");
            }

            System.out.println(horizontalLine);
        }
    }

    private static void printAdded(Task task, int size) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + size + " tasks in the list.");
    }
}