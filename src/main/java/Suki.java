/**
 * A personal assistant chatbot that keeps track of tasks.
 *
 * <p>This class only wires the pieces together and runs the main loop. The
 * actual work is delegated: {@link Ui} talks to the user, {@link Parser}
 * interprets what they typed, {@link TaskList} holds the tasks, and
 * {@link Storage} keeps them on disk.
 */
public class Suki {
    private static final String SAVE_FILE_PATH = "data/suki.txt";
    private static final String BYE_COMMAND = "bye";

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a Suki that saves to the given file, loading any tasks already
     * stored there.
     *
     * @param filePath where tasks are saved
     */
    public Suki(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            this.tasks = new TaskList(storage.load());
        } catch (SukiException e) {
            // A broken save file should not stop the user from working, so we
            // warn and start from an empty list instead of exiting.
            ui.showError(e.getMessage());
            ui.showLine();
            this.tasks = new TaskList();
        }
    }

    /** Reads and executes commands until the user says goodbye or input ends. */
    public void run() {
        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (input.trim().equals(BYE_COMMAND)) {
                break;
            }

            ui.showLine();
            try {
                execute(Parser.parse(input));
                storage.save(tasks.asArrayList());
            } catch (SukiException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }

        ui.close();
        ui.showGoodbye();
    }

    /** Carries out a single parsed command. */
    private void execute(Parser.ParsedInput parsed) throws SukiException {
        String arguments = parsed.arguments();

        switch (parsed.commandType()) {
        case LIST:
            ui.showTaskList(tasks);
            break;
        case MARK: {
            Task task = tasks.get(Parser.parseIndex(arguments, parsed.commandWord()));
            task.markAsDone();
            ui.showTaskMarked(task);
            break;
        }
        case UNMARK: {
            Task task = tasks.get(Parser.parseIndex(arguments, parsed.commandWord()));
            task.markAsNotDone();
            ui.showTaskUnmarked(task);
            break;
        }
        case DELETE: {
            Task removed = tasks.remove(Parser.parseIndex(arguments, parsed.commandWord()));
            ui.showTaskRemoved(removed, tasks.size());
            break;
        }
        case TODO:
            addTask(Parser.parseTodo(arguments));
            break;
        case DEADLINE:
            addTask(Parser.parseDeadline(arguments));
            break;
        case EVENT:
            addTask(Parser.parseEvent(arguments));
            break;
        default:
            throw new SukiException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private void addTask(Task task) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }

    public static void main(String[] args) {
        new Suki(SAVE_FILE_PATH).run();
    }
}
