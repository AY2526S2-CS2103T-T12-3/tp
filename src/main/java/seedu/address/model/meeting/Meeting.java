package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Meeting associated with a person in the address book.
 */
public class Meeting {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Meeting description should not be blank or null";
    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Meeting date must not be null";
    public static final String MESSAGE_INVALID_PARTICIPANT_IDS =
            "Meeting must have valid participant IDs";

    public static final String VALIDATION_REGEX = ".+"; // at least one character

    private final String description;
    private final LocalDate date;
    private final List<UUID> participantsID;

    /**
     * Constructs a {@code Meeting} with the specified description, date,
     * and a list of valid IDs of all participants.
     *
     * @param description Description of the meeting; must not be null or blank.
     * @param date Date of the meeting; must not be null.
     * @param participantsID List of participant IDs; must not be null or contain nulls.
     */
    public Meeting(String description, LocalDate date, List<UUID> participantsID) {
        requireNonNull(description, MESSAGE_DESCRIPTION_CONSTRAINTS);
        requireNonNull(date, MESSAGE_DATE_CONSTRAINTS);
        requireNonNull(participantsID, MESSAGE_INVALID_PARTICIPANT_IDS);

        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);

        // Validate each UUID individually
        List<UUID> validIds = new ArrayList<>();
        for (UUID id : participantsID) {
            if (id == null) {
                throw new IllegalArgumentException(MESSAGE_INVALID_PARTICIPANT_IDS);
            }
            validIds.add(id);
        }

        this.description = description;
        this.date = date;
        this.participantsID = validIds;
    }

    /**
     * Returns true if a given string is a valid meeting description.
     */
    public static boolean isValidDescription(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid date string.
     */
    public static boolean isValidDateString(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<UUID> getParticipantsID() {
        return participantsID;
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
