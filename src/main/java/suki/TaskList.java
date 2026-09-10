package suki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The list of tasks Suki is tracking.
 *
 * <p>This wraps an {@link ArrayList} instead of passing one around directly so
 * that list-related rules live in one place. In particular, index checking
 * happens here rather than being repeated by every caller.
 */
public class TaskList {
    /** The tasks, in the order the user added them. */
    private final ArrayList<Task> tasks;

    /** Creates an empty list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a list holding the given tasks, e.g. those loaded from disk.
     *
     * @param tasks the tasks to start with
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns how many tasks are in the list.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list has no tasks.
     *
     * @return true if there are no tasks
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given position.
     *
     * @param index zero-based position in the list
     * @return the task at that position
     * @throws SukiException if there is no task at that position
     */
    public Task get(int index) throws SukiException {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the given position.
     *
     * @param index zero-based position in the list
     * @return the task that was removed
     * @throws SukiException if there is no task at that position
     */
    public Task remove(int index) throws SukiException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Returns the tasks whose description contains the given text.
     *
     * <p>The match is case-insensitive and looks anywhere in the description,
     * so "book" finds "Return book" as well as "bookshop". Only the
     * description is searched: dates are excluded because a user looking for
     * "oct" almost certainly means the word, not October.
     *
     * @param keyword the text to look for
     * @return a new list holding the matching tasks, in their original order
     */
    public TaskList find(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return new TaskList(matches);
    }

    /**
     * Returns the tasks in order, for components such as {@link Storage} and
     * {@link Ui} that need to walk every task.
     *
     * <p>The view is unmodifiable so that callers can read the list without
     * being able to change it behind this class's back. Callers that want to
     * add or remove go through {@link #add} and {@link #remove}, which is what
     * keeps the bounds checking in one place.
     *
     * @return an unmodifiable view of the tasks, in the order they were added
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Rejects positions that fall outside the list.
     *
     * @param index the zero-based position to check
     * @throws SukiException if there is no task at that position
     */
    private void checkIndex(int index) throws SukiException {
        if (index < 0 || index >= tasks.size()) {
            throw new SukiException("That task number doesn't exist. You have "
                    + tasks.size() + " task(s).");
        }
    }
}
