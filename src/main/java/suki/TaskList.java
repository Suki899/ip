package suki;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
     * <p>Written as a stream because the search is exactly a filter over the
     * tasks: the stream says "keep the tasks whose description contains the
     * keyword" directly, where the equivalent loop says it indirectly, through
     * an accumulator list that the reader has to follow to see what is being
     * built.
     *
     * @param keyword the text to look for
     * @return a new list holding the matching tasks, in their original order
     */
    public TaskList find(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return new TaskList(tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Returns the underlying list, for components such as {@link Storage} that
     * need to walk every task.
     *
     * @return the backing list, not a copy
     */
    public ArrayList<Task> asArrayList() {
        return tasks;
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
