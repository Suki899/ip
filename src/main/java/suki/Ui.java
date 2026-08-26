package suki;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles everything the user sees and types.
 *
 * <p>Keeping all reading and printing in one class means the rest of Suki can
 * be tested and changed without worrying about console formatting, and a
 * different front end (a GUI, say) could replace this class alone.
 */
public class Ui {
    private static final String BANNER =
            " ____   _   _  _  __ ___ \n"
                    + "/ ___| | | | || |/ /|_ _|\n"
                    + "\\___ \\ | | | || ' /  | | \n"
                    + " ___) || |_| || . \\  | | \n"
                    + "|____/  \\___/ |_|\\_\\|___|\n";
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Returns true if the user has entered another line of input. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Returns the next line the user typed. */
    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hello! I'm Suki.");
        System.out.println("What can I do for you?");
        showLine();
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your list is empty.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        // Iterating the backing list avoids TaskList#get, whose bounds check
        // throws a checked exception that cannot happen for these indices.
        ArrayList<Task> list = tasks.asArrayList();
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + "." + list.get(i));
        }
    }

    /**
     * Prints the tasks matching a search, or a note if there were none.
     *
     * @param matches the tasks that matched
     */
    public void showFoundTasks(TaskList matches) {
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }
        System.out.println("Here are the matching tasks in your list:");
        ArrayList<Task> list = matches.asArrayList();
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + "." + list.get(i));
        }
    }

    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    public void showTaskRemoved(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    private void showTaskCount(int taskCount) {
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
