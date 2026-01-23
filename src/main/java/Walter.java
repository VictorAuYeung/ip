import java.util.Scanner;

public class Walter {
    public static void main(String[] args) {
        String horizontalLine = "    ____________________________________________________________";
        String greeting = "    ____________________________________________________________\n" +
                "     Hello! I'm Walter\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________";

        System.out.println(greeting);

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCounter = 0;

        while (true) {
            String input = scanner.nextLine();
            System.out.println(horizontalLine);

            if (input.equals("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println(horizontalLine);
                break;
            } else if (input.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCounter; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i].toString());
                }
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println("     Nice! I've marked this task as done:");
                System.out.println("       " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].unmarkAsDone();
                System.out.println("     OK, I've marked this task as not done yet:");
                System.out.println("       " + tasks[taskIndex]);
            } else {
                Task newTask = null;

                if (input.startsWith("todo ")) {
                    newTask = new Todo(input.substring(5));
                } else if (input.startsWith("deadline ")) {
                    String[] parts = input.substring(9).split(" /by ");
                    newTask = new Deadline(parts[0], parts[1]);
                } else if (input.startsWith("event ")) {
                    String[] parts = input.substring(6).split(" /from ");
                    String description = parts[0];
                    String[] timeParts = parts[1].split(" /to ");
                    newTask = new Event(description, timeParts[0], timeParts[1]);
                } else {
                    System.out.println("     OOPS!!! I'm sorry, but I don't know what that means AHHH :-(");
                    System.out.println(horizontalLine);
                    continue;
                }

                tasks[taskCounter] = newTask;
                taskCounter++;
                System.out.println("     Got it. I've added this task:");
                System.out.println("       " + newTask);
                System.out.println("     Now you have " + taskCounter + " tasks in the list.");
            }
            System.out.println(horizontalLine);
        }
        scanner.close();
    }
}