import java.util.Scanner;

public class Suki {
    private static final String BANNER =
            " ____   _   _  _  __ ___ \n"
                    + "/ ___| | | | || |/ /|_ _|\n"
                    + "\\___ \\ | | | || ' /  | | \n"
                    + " ___) || |_| || . \\  | | \n"
                    + "|____/  \\___/ |_|\\_\\|___|\n";
    private static final String LINE =
            "____________________________________________________________";
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Suki.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        String[] tasks = new String[MAX_TASKS];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals(BYE_COMMAND)) {
                break;
            }

            System.out.println(LINE);
            if (input.equals(LIST_COMMAND)) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println("added: " + input);
            }
            System.out.println(LINE);
        }
        scanner.close();

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
