/**
 * Represents a task that starts and ends at specific dates/times.
 */
public class Event extends Task {
    protected DateTime from;
    protected DateTime to;

    public Event(String description, DateTime from, DateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public DateTime getFrom() {
        return from;
    }

    public DateTime getTo() {
        return to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
