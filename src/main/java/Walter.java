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
                for (int i = 0; i < taskCounter; i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println(horizontalLine);
            } else if (input.equals("mark")) {

            } else {
                tasks[taskCounter] = input;
                taskCounter++;
                System.out.println("     added: " + input);
                System.out.println(horizontalLine);
            }
        }

        scanner.close();
    }
}