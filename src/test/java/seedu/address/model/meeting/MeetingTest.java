package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class MeetingTest {

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Meeting(null, VALID_DATE_20260325));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Meeting(VALID_DESCRIPTION_PROJECT, null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Meeting(INVALID_DESCRIPTION, VALID_DATE_20260325));
    }

    @Test
    public void constructor_validMeeting_success() {
        Meeting meeting = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);
        assertEquals(VALID_DESCRIPTION_PROJECT, meeting.getDescription());
        assertEquals(VALID_DATE_20260325, meeting.getDate());
    }

    @Test
    public void toStringMethod() {
        Meeting meeting = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);
        assertEquals(VALID_DESCRIPTION_PROJECT + " on " + VALID_DATE_20260325, meeting.toString());
    }

    @Test
    public void equals() {
        Meeting meeting1 = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);
        Meeting meeting2 = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);
        Meeting meetingDiffDescription = new Meeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260325);
        Meeting meetingDiffDate = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260401);

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
