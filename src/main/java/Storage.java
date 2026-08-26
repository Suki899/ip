import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads tasks from, and saves tasks to, a plain-text file on disk.
 *
 * <p>Each task occupies one line, with fields separated by " | ". The first
 * field is the task type icon, the second is 1 (done) or 0 (not done), and the
 * rest are type-specific. For example:
 * <pre>
 * T | 1 | read book
 * D | 0 | return book | Sunday
 * E | 0 | project meeting | Mon 2pm | 4pm
 * </pre>
 */
public class Storage {
    private static final String SEPARATOR = " | ";
    /** Used when splitting, because "|" is a regex metacharacter. */
    private static final String SEPARATOR_REGEX = " \\| ";

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the saved tasks from disk.
     *
     * <p>A missing file simply means there is nothing saved yet, so an empty
     * list is returned rather than an error. Individual lines that cannot be
     * understood are skipped so that one bad line does not lose the whole file.
     *
     * @return the tasks that were saved, in the order they were saved
     * @throws SukiException if the file exists but cannot be read
     */
    public ArrayList<Task> load() throws SukiException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                Task task = parseTask(scanner.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new SukiException("I couldn't read your saved tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Writes the given tasks to disk, replacing whatever was there before.
     *
     * <p>The parent directory is created if it does not already exist, so the
     * user does not have to set anything up by hand.
     *
     * @param tasks the tasks to save
     * @throws SukiException if the file cannot be written
     */
    public void save(ArrayList<Task> tasks) throws SukiException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new SukiException("I couldn't create the folder " + parent + " to save your tasks.");
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(encode(task) + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new SukiException("I couldn't save your tasks: " + e.getMessage());
        }
    }

    /**
     * Turns a task into the single line of text that represents it on disk.
     */
    private String encode(Task task) {
        String common = task.getTypeIcon() + SEPARATOR
                + (task.isDone() ? "1" : "0") + SEPARATOR
                + task.getDescription();
        if (task instanceof Deadline) {
            return common + SEPARATOR + ((Deadline) task).getBy().toStorageString();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return common + SEPARATOR + event.getFrom().toStorageString()
                    + SEPARATOR + event.getTo().toStorageString();
        }
        return common;
    }

    /**
     * Turns one saved line back into a task.
     *
     * @return the task, or null if the line is malformed and should be skipped
     */
    private Task parseTask(String line) {
        String[] parts = line.split(SEPARATOR_REGEX);
        if (parts.length < 3) {
            return null;
        }

        String typeIcon = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        try {
            switch (typeIcon) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    return null;
                }
                task = new Deadline(description, DateTime.parse(parts[3]));
                break;
            case "E":
                if (parts.length < 5) {
                    return null;
                }
                task = new Event(description, DateTime.parse(parts[3]), DateTime.parse(parts[4]));
                break;
            default:
                return null;
            }
        } catch (SukiException e) {
            // An unreadable date means this line is corrupt; skip it rather
            // than abandoning the rest of the file.
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
