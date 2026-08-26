/**
 * Represents a task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {
    protected DateTime by;

    public Deadline(String description, DateTime by) {
        super(description);
        this.by = by;
    }

    public DateTime getBy() {
        return by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
