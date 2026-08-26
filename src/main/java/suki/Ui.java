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
    /** The ASCII-art logo shown at startup. */
    private static final String BANNER =
            " ____   _   _  _  __ ___ \n"
                    + "/ ___| | | | || |/ /|_ _|\n"
                    + "\\___ \\ | | | || ' /  | | \n"
                    + " ___) || |_| || . \\  | | \n"
                    + "|____/  \\___/ |_|\\_\\|___|\n";
    /** The horizontal rule separating Suki's replies. */
    private static final String LINE =
            "____________________________________________________________";

    /** Reads the user's typed input. */
    private final Scanner scanner;

    /** Creates a Ui that reads from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Returns whether the user has entered another line of input.
     *
     * @return true if there is more input to read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next line the user typed.
     *
     * @return the line, without its trailing newline
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Stops reading input. Call this once, when the program is finishing. */
    public void close() {
        scanner.close();
    }

    /** Prints the horizontal rule used to separate Suki's replies. */
    public void showLine() {
        System.out.println(LINE);
    }

    /** Prints the banner and greeting shown when Suki starts. */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hello! I'm Suki.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /** Prints the parting message shown when the user leaves. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Prints an error in the standard format.
     *
     * @param message the explanation to show the user
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    /**
     * Prints the tasks, numbered from 1, or a note if there are none.
     *
     * @param tasks the tasks to show
     */
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

    /**
     * Confirms that a task was added.
     *
     * @param task the task that was added
     * @param taskCount how many tasks there are now
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task the task that was removed
     * @param taskCount how many tasks there are now
     */
    public void showTaskRemoved(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task is now done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Confirms that a task is no longer done.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Prints how many tasks remain, shared by the add and remove messages.
     *
     * @param taskCount how many tasks there are now
     */
    private void showTaskCount(int taskCount) {
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
