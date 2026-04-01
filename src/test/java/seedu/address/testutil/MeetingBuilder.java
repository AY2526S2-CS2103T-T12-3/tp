package seedu.address.testutil;

import static seedu.address.testutil.PersonBuilder.DEFAULT_ID;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.person.PersonId;

/**
 * A utility class to help with building {@code Meeting} objects.
 */
public class MeetingBuilder {

    public static final String DEFAULT_DESCRIPTION = "Project Meeting";
    public static final String DEFAULT_DATE = "2026-06-15";

    private Description description;
    private MeetingDate date;
    private Set<PersonId> participants = new HashSet<>();

    /**
     * Creates a {@code MeetingBuilder} with default details.
     */
    public MeetingBuilder() {
        this.description = new Description(DEFAULT_DESCRIPTION);
        this.date = new MeetingDate(DEFAULT_DATE);
        this.participants.add(new PersonId(DEFAULT_ID));
    }

    /**
     * Initializes the MeetingBuilder with the data of {@code meetingToCopy}.
     */
    public MeetingBuilder(Meeting meetingToCopy) {
        description = meetingToCopy.getDescription();
        date = meetingToCopy.getDate();
        participants = new HashSet<>(meetingToCopy.getParticipantsIDs());
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
     * Sets the participants from a set of String IDs. Converts them to PersonId.
     */
    public MeetingBuilder withParticipants(Set<String> participantIds) {
        this.participants = participantIds != null
                ? participantIds.stream().map(PersonId::new).collect(Collectors.toSet())
                : new HashSet<>();
        return this;
    }

    /**
     * Adds a single participant to the {@code Meeting}.
     */
    public MeetingBuilder addParticipant(String participantId) {
        this.participants.add(new PersonId(participantId));
        return this;
    }

    /**
     * Builds the {@code Meeting} object with all the set fields.
     */
    public Meeting build() {
        return new Meeting(description, date, participants);
    }
}
