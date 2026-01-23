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
                tasks[taskCounter] = new Task(input);
                taskCounter++;
                System.out.println("     added: " + input);
            }
            System.out.println(horizontalLine);
        }
        scanner.close();
    }
}