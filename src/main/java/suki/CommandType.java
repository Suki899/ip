package suki;

/**
 * Represents the type of command a user can enter.
 */
public enum CommandType {
    LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN;

    public static CommandType fromWord(String commandWord) {
        switch (commandWord) {
        case "list":
            return LIST;
        case "mark":
            return MARK;
        case "unmark":
            return UNMARK;
        case "delete":
            return DELETE;
        case "todo":
            return TODO;
        case "deadline":
            return DEADLINE;
        case "event":
            return EVENT;
        default:
            return UNKNOWN;
        }
    }
}
