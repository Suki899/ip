import java.util.ArrayList;
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
    private static final String SAVE_FILE_PATH = "data/suki.txt";

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Suki.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Storage storage = new Storage(SAVE_FILE_PATH);
        ArrayList<Task> tasks;
        try {
            tasks = storage.load();
        } catch (SukiException e) {
            // A broken save file should not stop the user from working, so we
            // warn and start from an empty list instead of exiting.
            System.out.println("OOPS!!! " + e.getMessage());
            System.out.println(LINE);
            tasks = new ArrayList<>();
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals(BYE_COMMAND)) {
                break;
            }

            System.out.println(LINE);
            try {
                processCommand(input, tasks);
                storage.save(tasks);
            } catch (SukiException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }
            System.out.println(LINE);
        }
        scanner.close();

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    private static void processCommand(String input, ArrayList<Task> tasks) throws SukiException {
        String[] split = input.split(" ", 2);
        String commandWord = split[0];
        String arguments = split.length > 1 ? split[1].trim() : "";
        CommandType commandType = CommandType.fromWord(commandWord);

        switch (commandType) {
        case LIST:
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
            break;
        case MARK: {
            int index = parseIndex(arguments, commandWord, tasks.size());
            tasks.get(index).markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks.get(index));
            break;
        }
        case UNMARK: {
            int index = parseIndex(arguments, commandWord, tasks.size());
            tasks.get(index).markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks.get(index));
            break;
        }
        case DELETE: {
            int index = parseIndex(arguments, commandWord, tasks.size());
            Task removed = tasks.remove(index);
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removed);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            break;
        }
        case TODO:
            if (arguments.isEmpty()) {
                throw new SukiException("The description of a todo cannot be empty.");
            }
            addTask(tasks, new Todo(arguments));
            break;
        case DEADLINE: {
            if (arguments.isEmpty()) {
                throw new SukiException("The description of a deadline cannot be empty.");
            }
            String[] parts = arguments.split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new SukiException("A deadline needs a description and a '/by' date/time, "
                        + "e.g. deadline return book /by 2019-10-15");
            }
            addTask(tasks, new Deadline(parts[0].trim(), DateTime.parse(parts[1])));
            break;
        }
        case EVENT: {
            if (arguments.isEmpty()) {
                throw new SukiException("The description of an event cannot be empty.");
            }
            String[] fromSplit = arguments.split(" /from ", 2);
            if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
                throw new SukiException("An event needs a description, a '/from' and a '/to' date/time, "
                        + "e.g. event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
            }
            String[] toSplit = fromSplit[1].split(" /to ", 2);
            if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
                throw new SukiException("An event needs a description, a '/from' and a '/to' date/time, "
                        + "e.g. event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
            }
            addTask(tasks, new Event(fromSplit[0].trim(),
                    DateTime.parse(toSplit[0]), DateTime.parse(toSplit[1])));
            break;
        }
        default:
            throw new SukiException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private static int parseIndex(String arguments, String command, int taskCount) throws SukiException {
        int index;
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new SukiException("Please provide a valid task number, e.g. " + command + " 1");
        }
        if (index < 0 || index >= taskCount) {
            throw new SukiException("That task number doesn't exist. You have " + taskCount + " task(s).");
        }
        return index;
    }

    private static void addTask(ArrayList<Task> tasks, Task newTask) {
        tasks.add(newTask);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }
}
