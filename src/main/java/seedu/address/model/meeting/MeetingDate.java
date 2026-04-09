package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a Meeting's date in the address book.
 * Guarantees: immutable; is valid in yyyy-MM-dd format.
 */
public class MeetingDate implements Comparable<MeetingDate> {
    public static final String MESSAGE_DATE_NON_NULL =
            "Meeting date must not be null";
    public static final String MESSAGE_DATE_WRONG_FORMAT =
            "Meeting date must be in yyyy-MM-dd format.";
    public static final String MESSAGE_INVALID_DATE =
            "Meeting date must be a valid calendar date.";

    public final LocalDate date;

    /**
     * Constructs a {@code Date}.
     *
     * @param dateString A date string to validate.
     */
    public MeetingDate(String dateString) {
        requireNonNull(dateString, MESSAGE_DATE_NON_NULL);
        checkArgument(isValidDateFormat(dateString), MESSAGE_DATE_WRONG_FORMAT);
        checkArgument(isValidDate(dateString), MESSAGE_INVALID_DATE);
        this.date = LocalDate.parse(dateString);
    }

    /**
     * Returns true if a given string is in yyyy-MM-dd format.
     */
    public static boolean isValidDateFormat(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * Returns true if a given string represents a valid date in the calendar.
     */
    public static boolean isValidDate(String test) {
        requireNonNull(test);
        try {
            LocalDate.parse(test);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isBefore(LocalDate time) {
        return date.isBefore(time);
    }

    public boolean isAfter(LocalDate time) {
        return date.isAfter(time);
    }

    @Override
    public int compareTo(MeetingDate o) {
        return this.date.compareTo(o.getDate());
    }

    /**
     * Returns the string {@code (past date)} if {@code date} is before system date.
     * Otherwise, returns an empty string.
     */
    public String getPassedDateNotification() {
        if (this.date.isBefore(LocalDate.now())) {
            return " (past date)";
        }
        return "";
    }

    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MeetingDate)) {
            return false;
        }
        MeetingDate otherDate = (MeetingDate) other;
        return date.equals(otherDate.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
