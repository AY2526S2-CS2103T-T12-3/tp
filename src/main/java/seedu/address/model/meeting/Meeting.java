package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a Meeting associated with a person in the address book.
 */
public class Meeting {
    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Meeting date must not be null";
    public static final String MESSAGE_INVALID_PARTICIPANT_IDS =
            "Meeting must have valid participant IDs";

    private final Description description;
    private final LocalDate date;
    private final Set<UUID> participantsID;
    public static final String VALIDATION_REGEX = "\\d{4}-\\d{2}-\\d{2}";

    /**
     * Constructs a {@code Meeting} with the specified description, date,
     * and a set of valid IDs of all participants.
     *
     * @param description Description of the meeting; must not be null or blank.
     * @param date Date of the meeting; must not be null.
     * @param participantsID Set of participant IDs; must not be null or contain nulls.
     */
    public Meeting(Description description, LocalDate date, Set<UUID> participantsID) {
        requireNonNull(description);
        requireNonNull(date, MESSAGE_DATE_CONSTRAINTS);
        requireNonNull(participantsID, MESSAGE_INVALID_PARTICIPANT_IDS);

        // Validate each UUID individually
        for (UUID id : participantsID) {
            if (id == null) {
                throw new IllegalArgumentException(MESSAGE_INVALID_PARTICIPANT_IDS);
            }
        }

        this.description = description;
        this.date = date;
        this.participantsID = new HashSet<>(participantsID);;
    }

    /**
     * Returns true if a given string is a valid date string.
     */
    public static boolean isValidDateString(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    public Description getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return LocalDate.from(date); // Returns a defensive copy of the date.
    }

    public Set<UUID> getParticipantsID() {
        return new HashSet<>(participantsID); // Returns a defensive copy of the set.
    }

    /**
     * Returns true if two meetings are the same.
     * Two meetings are the same if their descriptions and date are equal.
     */
    public boolean isSameMeeting(Meeting otherMeeting) {
        return description.equals(otherMeeting.description)
                && date.equals(otherMeeting.date);
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
                && date.equals(otherMeeting.date)
                && participantsID.equals(otherMeeting.participantsID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, date, participantsID);
    }

    /**
     * Formats state as text for viewing.
     */
    @Override
    public String toString() {
        return description + " on " + date;
    }
}
