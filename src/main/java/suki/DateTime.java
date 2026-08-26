package suki;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * A date, optionally with a time of day, as supplied by the user.
 *
 * <p>This wraps {@link LocalDateTime} rather than using it directly because
 * Suki needs to remember whether the user actually gave a time. "2019-10-15"
 * and "2019-10-15 0000" are the same instant, but only the second one should
 * be shown with a clock time attached.
 *
 * <p>A simpler alternative would be to always store a {@code LocalDateTime} and
 * always print the time; that was rejected because it would show a misleading
 * "12:00AM" for dates the user gave without one.
 */
public class DateTime implements Comparable<DateTime> {
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATE_TIME =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mma", Locale.ENGLISH);

    /** Input patterns accepted for a date with a time, tried in order. */
    private static final DateTimeFormatter[] DATE_TIME_PATTERNS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("d/M/yyyy HHmm", Locale.ENGLISH),
    };

    /** Input patterns accepted for a bare date, tried in order. */
    private static final DateTimeFormatter[] DATE_PATTERNS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH),
    };

    private final LocalDateTime value;
    private final boolean hasTime;

    private DateTime(LocalDateTime value, boolean hasTime) {
        this.value = value;
        this.hasTime = hasTime;
    }

    /**
     * Reads a date/time written by the user.
     *
     * <p>Accepted forms are {@code 2019-10-15}, {@code 2019-10-15 1800},
     * {@code 15/10/2019} and {@code 15/10/2019 1800}.
     *
     * @param input the text the user typed
     * @return the parsed date, remembering whether a time was given
     * @throws SukiException if the text matches none of the accepted forms
     */
    public static DateTime parse(String input) throws SukiException {
        String trimmed = input.trim();

        for (DateTimeFormatter pattern : DATE_TIME_PATTERNS) {
            try {
                return new DateTime(LocalDateTime.parse(trimmed, pattern), true);
            } catch (DateTimeParseException e) {
                // Not this pattern; fall through and try the next one.
            }
        }

        for (DateTimeFormatter pattern : DATE_PATTERNS) {
            try {
                return new DateTime(LocalDate.parse(trimmed, pattern).atStartOfDay(), false);
            } catch (DateTimeParseException e) {
                // Not this pattern; fall through and try the next one.
            }
        }

        throw new SukiException("I don't understand the date '" + trimmed + "'. "
                + "Try yyyy-MM-dd or d/M/yyyy, optionally followed by a 24-hour time, "
                + "e.g. 2019-10-15 1800");
    }

    /**
     * Returns this date in a form that {@link #parse} can read back, so that
     * saving and reloading a task does not lose the time of day.
     */
    public String toStorageString() {
        return hasTime
                ? value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH))
                : value.toLocalDate().toString();
    }

    @Override
    public int compareTo(DateTime other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return hasTime ? value.format(DISPLAY_DATE_TIME) : value.format(DISPLAY_DATE);
    }
}
