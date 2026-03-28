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

public class MeetingTest {
    private static final UUID VALID_UUID_1 =
            UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID VALID_UUID_2 =
            UUID.fromString("00000000-0000-0000-0000-000000000002");

    public static final Set<UUID> VALID_UUIDS = Set.of(VALID_UUID_1, VALID_UUID_2);

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Meeting(null,
                VALID_DATE_20260325, VALID_UUIDS));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Meeting(VALID_DESCRIPTION_PROJECT,
                null, VALID_UUIDS));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Meeting(INVALID_DESCRIPTION,
                VALID_DATE_20260325, VALID_UUIDS));
    }

    @Test
    public void constructor_validMeeting_success() {
        Meeting meeting = new Meeting(VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20260325, VALID_UUIDS);
        assertEquals(VALID_DESCRIPTION_PROJECT, meeting.getDescription());
        assertEquals(VALID_DATE_20260325, meeting.getDate());
        assertEquals(VALID_UUIDS, meeting.getParticipantsID());
    }

    @Test
    public void toStringMethod() {
        Meeting meeting = new Meeting(VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20260325, VALID_UUIDS);
        assertEquals(VALID_DESCRIPTION_PROJECT + " on " + VALID_DATE_20260325, meeting.toString());
    }

    @Test
    public void equals() {
        Meeting meeting1 = new Meeting(VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20260325, VALID_UUIDS);
        Meeting meeting2 = new Meeting(VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20260325, VALID_UUIDS);
        Meeting meetingDiffDescription = new Meeting(VALID_DESCRIPTION_TEAM,
                VALID_DATE_20260325, VALID_UUIDS);
        Meeting meetingDiffDate = new Meeting(VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20260401, VALID_UUIDS);

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
