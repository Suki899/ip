import java.util.ArrayList;

/**
 * The list of tasks Suki is tracking.
 *
 * <p>This wraps an {@link ArrayList} instead of passing one around directly so
 * that list-related rules live in one place. In particular, index checking
 * happens here rather than being repeated by every caller.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Creates a list holding the given tasks, e.g. those loaded from disk. */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given position.
     *
     * @param index zero-based position in the list
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
     * @throws SukiException if there is no task at that position
     */
    public Task remove(int index) throws SukiException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Returns the underlying list, for components such as {@link Storage} that
     * need to walk every task.
     */
    public ArrayList<Task> asArrayList() {
        return tasks;
    }

    private void checkIndex(int index) throws SukiException {
        if (index < 0 || index >= tasks.size()) {
            throw new SukiException("That task number doesn't exist. You have "
                    + tasks.size() + " task(s).");
        }
    }
}
