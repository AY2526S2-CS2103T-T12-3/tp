package seedu.address.testutil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDate;

/**
 * A utility class to help with building {@code Meeting} objects.
 */
public class MeetingBuilder {

    public static final String DEFAULT_DESCRIPTION = "Project Meeting";
    public static final String DEFAULT_DATE = "2026-06-15";
    public static final Set<UUID> DEFAULT_PARTICIPANTS = new HashSet<>();

    private Description description;
    private MeetingDate date;
    private Set<UUID> participants;

    /**
     * Creates a {@code MeetingBuilder} with default details.
     */
    public MeetingBuilder() {
        description = new Description(DEFAULT_DESCRIPTION);
        date = new MeetingDate(DEFAULT_DATE);
        participants = new HashSet<>(DEFAULT_PARTICIPANTS);
    }

    /**
     * Initializes the MeetingBuilder with the data of {@code meetingToCopy}.
     */
    public MeetingBuilder(Meeting meetingToCopy) {
        description = meetingToCopy.getDescription();
        date = meetingToCopy.getDate();
        participants = new HashSet<>(meetingToCopy.getParticipantsID());
    }

    /**
     * Sets the {@code description} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code date} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withDate(String date) {
        this.date = new MeetingDate(date);
        return this;
    }

    /**
     * Sets the {@code participants} of the {@code Meeting}.
     */
    public MeetingBuilder withParticipants(Set<UUID> participants) {
        this.participants = participants != null ? new HashSet<>(participants) : new HashSet<>();
        return this;
    }

    /**
     * Adds a single participant to the {@code Meeting}.
     */
    public MeetingBuilder addParticipant(UUID participantId) {
        this.participants.add(participantId);
        return this;
    }

    /**
     * Builds the {@code Meeting} object with all the set fields.
     */
    public Meeting build() {
        return new Meeting(description, date, participants);
    }
}
