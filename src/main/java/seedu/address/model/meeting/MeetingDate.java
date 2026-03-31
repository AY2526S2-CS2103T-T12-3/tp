package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a Meeting's date in the address book.
 * Guarantees: immutable; is valid in yyyy-MM-dd format.
 */
public class MeetingDate {
    public static final String MESSAGE_DATE_NON_NULL =
            "Meeting date must not be null";
    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Meeting date must be in yyyy-MM-dd format, and must be a valid calendar date.";

    public final LocalDate date;

    /**
     * Constructs a {@code Date}.
     *
     * @param dateString A valid date string in yyyy-MM-dd format.
     */
    public MeetingDate(String dateString) {
        requireNonNull(dateString, MESSAGE_DATE_NON_NULL);
        checkArgument(isValidDateString(dateString), MESSAGE_DATE_CONSTRAINTS);
        this.date = LocalDate.parse(dateString);
    }

    /**
     * Returns true if a given string is a valid date in yyyy-MM-dd format.
     */
    public static boolean isValidDateString(String test) {
        requireNonNull(test);
        try {
            LocalDate.parse(test); // validates both format + real date
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date.toString(); // yyyy-MM-dd
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
