package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a Meeting associated with a person in the address book.
 */
public class Meeting {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Meeting description should not be blank or null";
    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Meeting date must not be null";

    public static final String VALIDATION_REGEX = ".+"; // at least one character

    private final String description;
    private final LocalDate date;

    /**
     * Constructs a {@code Meeting} with the specified description and date.
     *
     * @param description Description of the meeting; must not be null or blank.
     * @param date Date of the meeting; must not be null.
     */
    public Meeting(String description, LocalDate date) {
        requireNonNull(description, MESSAGE_DESCRIPTION_CONSTRAINTS);
        requireNonNull(date, MESSAGE_DATE_CONSTRAINTS);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);

        this.description = description;
        this.date = date;
    }

    /**
     * Returns true if a given string is a valid meeting description.
     */
    public static boolean isValidDescription(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Meeting)) {
            return false;
        }
        Meeting otherMeeting = (Meeting) other;
        return description.equals(otherMeeting.description)
                && date.equals(otherMeeting.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, date);
    }

    /**
     * Formats state as text for viewing.
     */
    @Override
    public String toString() {
        return description + " on " + date;
    }
}
