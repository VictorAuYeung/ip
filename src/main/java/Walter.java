import java.util.Scanner;

public class Walter {
    public static void main(String[] args) {
        String horizontalLine = "    ____________________________________________________________";
        String greeting = "____________________________________________________________\n" +
                " Hello! I'm Walter\n" +
                " What can I do for you?\n" +
                "____________________________________________________________";

        String exit_msg = " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________";
        System.out.println(greeting);

        Scanner scanner = new Scanner(System.in);


        while (true) {
            String input = scanner.nextLine();

            System.out.println(horizontalLine);

            if (input.equals("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println(horizontalLine);
                break;
            }

            System.out.println("     " + input);
            System.out.println(horizontalLine);
        }

        scanner.close();
    }
}
