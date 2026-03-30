package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.MeetingBuilder;

public class MeetingTest {
    private static final UUID VALID_UUID_1 =
            UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID VALID_UUID_2 =
            UUID.fromString("00000000-0000-0000-0000-000000000002");

    public static final Set<UUID> VALID_UUIDS = Set.of(VALID_UUID_1, VALID_UUID_2);

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new MeetingBuilder().withDescription(null).withDate(VALID_DATE_20260325)
                        .withParticipants(VALID_UUIDS).build());
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new MeetingBuilder().withDescription(VALID_DESCRIPTION_PROJECT).withDate(null)
                        .withParticipants(VALID_UUIDS).build());
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new MeetingBuilder().withDescription(INVALID_DESCRIPTION)
                        .withDate(VALID_DATE_20260325).withParticipants(VALID_UUIDS).build());
    }

    @Test
    public void constructor_validMeeting_success() {
        Meeting meeting = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20260325)
                .withParticipants(VALID_UUIDS)
                .build();
        assertEquals(new Description(VALID_DESCRIPTION_PROJECT), meeting.getDescription());
        assertEquals(VALID_DATE_20260325, meeting.getDate());
        assertEquals(VALID_UUIDS, meeting.getParticipantsID());
    }

    @Test
    public void toStringMethod() {
        Meeting meeting = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20260325)
                .withParticipants(VALID_UUIDS)
                .build();
        assertEquals(VALID_DESCRIPTION_PROJECT + " on " + VALID_DATE_20260325, meeting.toString());
    }

    @Test
    public void equals() {
        Meeting meeting1 = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20260325)
                .withParticipants(VALID_UUIDS)
                .build();
        Meeting meeting2 = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20260325)
                .withParticipants(VALID_UUIDS)
                .build();
        Meeting meetingDiffDescription = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_TEAM)
                .withDate(VALID_DATE_20260325)
                .withParticipants(VALID_UUIDS)
                .build();
        Meeting meetingDiffDate = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20260401)
                .withParticipants(VALID_UUIDS)
                .build();

        // same object
        assertEquals(meeting1, meeting1);

        // same values
        assertEquals(meeting1, meeting2);

        // different description
        assertNotEquals(meeting1, meetingDiffDescription);

        // different date
        assertNotEquals(meeting1, meetingDiffDate);
    }
}
