package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.PersonId;

/**
 * Represents a Meeting associated with a person in the address book.
 */
public class Meeting {
    public static final String MESSAGE_INVALID_PARTICIPANT_IDS =
            "Meeting must have valid participant IDs";

    private final Description description;
    private final MeetingDate date;
    private final Set<PersonId> participantsID;

    /**
     * Constructs a {@code Meeting} with the specified description, date,
     * and a set of valid IDs of all participants.
     *
     * @param description Description of the meeting; must not be null or blank.
     * @param date Date of the meeting; must not be null.
     * @param participantsID Set of participant IDs; must not be null or contain nulls.
     */
    public Meeting(Description description, MeetingDate date, Set<PersonId> participantsID) {
        assert description != null : "description should not be null";
        assert date != null : "date should not be null";
        assert participantsID != null : "participantsID should not be null";

        requireAllNonNull(description, date);
        requireNonNull(participantsID, MESSAGE_INVALID_PARTICIPANT_IDS);

        // Validate each PersonId individually
        for (PersonId id : participantsID) {
            assert id != null : "Participant ID should not be null";
            requireNonNull(id, MESSAGE_INVALID_PARTICIPANT_IDS);
        }

        this.description = description;
        this.date = date;
        this.participantsID = new HashSet<>(participantsID);
    }

    // Returns a defensive copy of the parameters.
    public Description getDescription() {
        assert description != null : "description should not be null when accessed";
        return new Description(description.toString());
    }

    public MeetingDate getDate() {
        assert date != null : "date should not be null when accessed";
        return new MeetingDate(date.toString());
    }

    public Set<PersonId> getParticipantsIDs() {
        assert participantsID != null : "participantsID should not be null when accessed";
        return new HashSet<>(participantsID);
    }

    /**
     * Returns true if two meetings are the same.
     * Two meetings are the same if their descriptions and date are equal.
     */
    public boolean isSameMeeting(Meeting otherMeeting) {
        assert otherMeeting != null : "otherMeeting should not be null";
        return description.equals(otherMeeting.description)
                && date.equals(otherMeeting.date);
    }

    /**
     * Returns true if both meetings have the same identity and participants.
     * This defines a stronger notion of equality between two meetings.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Meeting)) {
            return false;
        }
        Meeting otherMeeting = (Meeting) other;

        assert otherMeeting.description != null : "otherMeeting description should not be null";
        assert otherMeeting.date != null : "otherMeeting date should not be null";
        assert otherMeeting.participantsID != null : "otherMeeting participants should not be null";

        return description.equals(otherMeeting.description)
                && date.equals(otherMeeting.date)
                && participantsID.equals(otherMeeting.participantsID);
    }

    @Override
    public int hashCode() {
        assert description != null : "description should not be null when hashing";
        assert date != null : "date should not be null when hashing";
        assert participantsID != null : "participantsID should not be null when hashing";
        return Objects.hash(description, date, participantsID);
    }

    /**
     * Formats state as text for viewing.
     */
    @Override
    public String toString() {
        assert description != null : "description should not be null when converting to string";
        assert date != null : "date should not be null when converting to string";
        return description + " on " + date;
    }
}
