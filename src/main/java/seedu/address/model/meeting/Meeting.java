package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
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
    private final Set<PersonId> participantsIDs;

    /**
     * Constructs a {@code Meeting} with the specified description, date,
     * and a set of valid IDs of all participants.
     *
     * @param description Description of the meeting; must not be null or blank.
     * @param date Date of the meeting; must not be null.
     * @param participantsIDs Set of participant IDs; must not be null or contain nulls.
     */
    public Meeting(Description description, MeetingDate date, Set<PersonId> participantsIDs) {
        requireAllNonNull(description, date, participantsIDs);

        for (PersonId id : participantsIDs) {
            requireNonNull(id, MESSAGE_INVALID_PARTICIPANT_IDS);
        }

        this.description = description;
        this.date = date;
        this.participantsIDs = new HashSet<>(participantsIDs);

        assert this.description != null : "description should not be null";
        assert this.date != null : "date should not be null";
    }

    public Description getDescription() {
        assert description != null : "description should not be null";
        return new Description(description.toString());
    }

    public MeetingDate getDate() {
        assert date != null : "date should not be null";
        return new MeetingDate(date.toString());
    }

    public Set<PersonId> getParticipantsIDs() {
        return new HashSet<>(participantsIDs);
    }

    /**
     * Returns true if two meetings are the same.
     * Two meetings are the same if their descriptions and date are equal.
     */
    public boolean isSameMeeting(Meeting otherMeeting) {
        assert otherMeeting != null : "otherMeeting should not be null";;
        return description.equals(otherMeeting.description)
                && date.equals(otherMeeting.date);
    }

    public boolean isBefore(LocalDate time) {
        return date.isBefore(time);
    }

    public boolean isAfter(LocalDate time) {
        return date.isAfter(time);
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
        assert otherMeeting != null : "otherMeeting should not be null";;

        return description.equals(otherMeeting.description)
                && date.equals(otherMeeting.date)
                && participantsIDs.equals(otherMeeting.participantsIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, date, participantsIDs);
    }

    /**
     * Formats state as text for viewing.
     */
    @Override
    public String toString() {
        return description + " on " + date;
    }
}
