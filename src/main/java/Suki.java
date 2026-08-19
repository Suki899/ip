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
            try {
                taskCount = processCommand(input, tasks, taskCount);
            } catch (SukiException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }
            System.out.println(LINE);
        }
        scanner.close();

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    private static int processCommand(String input, Task[] tasks, int taskCount) throws SukiException {
        if (input.equals(LIST_COMMAND)) {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println((i + 1) + "." + tasks[i]);
            }
            return taskCount;
        } else if (input.equals(MARK_COMMAND) || input.startsWith(MARK_COMMAND + " ")) {
            int index = parseIndex(input, MARK_COMMAND, taskCount);
            tasks[index].markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks[index]);
            return taskCount;
        } else if (input.equals(UNMARK_COMMAND) || input.startsWith(UNMARK_COMMAND + " ")) {
            int index = parseIndex(input, UNMARK_COMMAND, taskCount);
            tasks[index].markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks[index]);
            return taskCount;
        } else if (input.equals(TODO_COMMAND) || input.startsWith(TODO_COMMAND + " ")) {
            String description = input.equals(TODO_COMMAND) ? "" : input.substring(TODO_COMMAND.length() + 1).trim();
            if (description.isEmpty()) {
                throw new SukiException("The description of a todo cannot be empty.");
            }
            return addTask(tasks, taskCount, new Todo(description));
        } else if (input.equals(DEADLINE_COMMAND) || input.startsWith(DEADLINE_COMMAND + " ")) {
            String details = input.equals(DEADLINE_COMMAND) ? "" : input.substring(DEADLINE_COMMAND.length() + 1).trim();
            if (details.isEmpty()) {
                throw new SukiException("The description of a deadline cannot be empty.");
            }
            String[] parts = details.split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new SukiException("A deadline needs a description and a '/by' date/time, "
                        + "e.g. deadline return book /by Sunday");
            }
            return addTask(tasks, taskCount, new Deadline(parts[0].trim(), parts[1].trim()));
        } else if (input.equals(EVENT_COMMAND) || input.startsWith(EVENT_COMMAND + " ")) {
            String details = input.equals(EVENT_COMMAND) ? "" : input.substring(EVENT_COMMAND.length() + 1).trim();
            if (details.isEmpty()) {
                throw new SukiException("The description of an event cannot be empty.");
            }
            String[] fromSplit = details.split(" /from ", 2);
            if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
                throw new SukiException("An event needs a description, a '/from' and a '/to' date/time, "
                        + "e.g. event project meeting /from Mon 2pm /to 4pm");
            }
            String[] toSplit = fromSplit[1].split(" /to ", 2);
            if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
                throw new SukiException("An event needs a description, a '/from' and a '/to' date/time, "
                        + "e.g. event project meeting /from Mon 2pm /to 4pm");
            }
            return addTask(tasks, taskCount, new Event(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim()));
        } else {
            throw new SukiException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private static int parseIndex(String input, String command, int taskCount) throws SukiException {
        String indexPart = input.equals(command) ? "" : input.substring(command.length() + 1).trim();
        int index;
        try {
            index = Integer.parseInt(indexPart) - 1;
        } catch (NumberFormatException e) {
            throw new SukiException("Please provide a valid task number, e.g. " + command + " 1");
        }
        if (index < 0 || index >= taskCount) {
            throw new SukiException("That task number doesn't exist. You have " + taskCount + " task(s).");
        }
        return index;
    }

    private static int addTask(Task[] tasks, int taskCount, Task newTask) throws SukiException {
        if (taskCount >= MAX_TASKS) {
            throw new SukiException("Your task list is full. Please clear some tasks first.");
        }
        tasks[taskCount] = newTask;
        int newTaskCount = taskCount + 1;
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask);
        System.out.println("Now you have " + newTaskCount + " tasks in the list.");
        return newTaskCount;
    }
}
