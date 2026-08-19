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
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Suki.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals(BYE_COMMAND)) {
                break;
            }

            System.out.println(LINE);
            if (input.equals(LIST_COMMAND)) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith(MARK_COMMAND + " ")) {
                int index = Integer.parseInt(input.substring(MARK_COMMAND.length() + 1).trim()) - 1;
                tasks[index].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[index]);
            } else if (input.startsWith(UNMARK_COMMAND + " ")) {
                int index = Integer.parseInt(input.substring(UNMARK_COMMAND.length() + 1).trim()) - 1;
                tasks[index].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[index]);
            } else if (input.startsWith(TODO_COMMAND + " ")) {
                String description = input.substring(TODO_COMMAND.length() + 1);
                tasks[taskCount] = new Todo(description);
                taskCount = addTask(tasks, taskCount);
            } else if (input.startsWith(DEADLINE_COMMAND + " ")) {
                String details = input.substring(DEADLINE_COMMAND.length() + 1);
                String[] parts = details.split(" /by ", 2);
                tasks[taskCount] = new Deadline(parts[0], parts[1]);
                taskCount = addTask(tasks, taskCount);
            } else if (input.startsWith(EVENT_COMMAND + " ")) {
                String details = input.substring(EVENT_COMMAND.length() + 1);
                String[] fromSplit = details.split(" /from ", 2);
                String[] toSplit = fromSplit[1].split(" /to ", 2);
                tasks[taskCount] = new Event(fromSplit[0], toSplit[0], toSplit[1]);
                taskCount = addTask(tasks, taskCount);
            }
            System.out.println(LINE);
        }
        scanner.close();

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    private static int addTask(Task[] tasks, int taskCount) {
        int newTaskCount = taskCount + 1;
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        System.out.println("Now you have " + newTaskCount + " tasks in the list.");
        return newTaskCount;
    }
}
