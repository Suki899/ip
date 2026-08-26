package suki;

/**
 * Turns raw lines typed by the user into something Suki can act on.
 *
 * <p>All knowledge of the command syntax — where the spaces and slashes go —
 * is confined to this class, so changing the syntax does not ripple into the
 * main loop or the task classes.
 */
public class Parser {

    /**
     * The result of reading one line of input: which command it is, and
     * whatever text followed the command word.
     *
     * <p>A record is used because this is a plain carrier of two values with no
     * behaviour of its own.
     */
    public record ParsedInput(CommandType commandType, String commandWord, String arguments) {
    }

    /**
     * Splits a line into its command word and the arguments that follow.
     *
     * @param input a full line typed by the user
     * @return the command type and its arguments
     */
    public static ParsedInput parse(String input) {
        String[] split = input.trim().split(" ", 2);
        String commandWord = split[0];
        String arguments = split.length > 1 ? split[1].trim() : "";
        return new ParsedInput(CommandType.fromWord(commandWord), commandWord, arguments);
    }

    /**
     * Reads a one-based task number as typed by the user and converts it to a
     * zero-based list index.
     *
     * @param arguments the text following the command word
     * @param commandWord the command being run, used only to build the error message
     * @return the zero-based index
     * @throws SukiException if the text is not a number
     */
    public static int parseIndex(String arguments, String commandWord) throws SukiException {
        try {
            return Integer.parseInt(arguments.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new SukiException("Please provide a valid task number, e.g. " + commandWord + " 1");
        }
    }

    /**
     * Builds a {@link Todo} from the arguments of a "todo" command.
     *
     * @throws SukiException if no description was given
     */
    public static Todo parseTodo(String arguments) throws SukiException {
        if (arguments.isEmpty()) {
            throw new SukiException("The description of a todo cannot be empty.");
        }
        return new Todo(arguments);
    }

    /**
     * Builds a {@link Deadline} from the arguments of a "deadline" command,
     * which must be of the form {@code <description> /by <date>}.
     *
     * @throws SukiException if the description or the date is missing or unreadable
     */
    public static Deadline parseDeadline(String arguments) throws SukiException {
        String[] parts = arguments.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new SukiException("A deadline needs a description and a '/by' date/time, "
                    + "e.g. deadline return book /by 2019-10-15");
        }
        return new Deadline(parts[0].trim(), DateTime.parse(parts[1]));
    }

    /**
     * Builds an {@link Event} from the arguments of an "event" command, which
     * must be of the form {@code <description> /from <date> /to <date>}.
     *
     * @throws SukiException if any part is missing or unreadable
     */
    public static Event parseEvent(String arguments) throws SukiException {
        String[] fromSplit = arguments.split(" /from ", 2);
        if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
            throw new SukiException(eventFormatMessage());
        }
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new SukiException(eventFormatMessage());
        }
        return new Event(fromSplit[0].trim(), DateTime.parse(toSplit[0]), DateTime.parse(toSplit[1]));
    }

    private static String eventFormatMessage() {
        return "An event needs a description, a '/from' and a '/to' date/time, "
                + "e.g. event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600";
    }
}
