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


        System.out.println(exit_msg);
    }
}
