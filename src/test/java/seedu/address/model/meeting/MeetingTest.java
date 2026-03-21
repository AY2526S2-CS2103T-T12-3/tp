package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TEAM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_20260325;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_20260401;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class MeetingTest {

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        LocalDate validDate = LocalDate.parse(VALID_DATE_20260325);
        assertThrows(NullPointerException.class, () -> new Meeting(null, validDate));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Meeting(VALID_DESCRIPTION_PROJECT, null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        LocalDate validDate = LocalDate.parse(VALID_DATE_20260325);
        assertThrows(IllegalArgumentException.class, () -> new Meeting(INVALID_DESCRIPTION, validDate));
    }

    @Test
    public void constructor_validMeeting_success() {
        LocalDate validDate = LocalDate.parse(VALID_DATE_20260325);
        Meeting meeting = new Meeting(VALID_DESCRIPTION_PROJECT, validDate);
        assertEquals(VALID_DESCRIPTION_PROJECT, meeting.getDescription());
        assertEquals(validDate, meeting.getDate());
    }

    @Test
    public void toStringMethod() {
        LocalDate validDate = LocalDate.parse(VALID_DATE_20260325);
        Meeting meeting = new Meeting(VALID_DESCRIPTION_PROJECT, validDate);
        assertEquals(VALID_DESCRIPTION_PROJECT + " on " + VALID_DATE_20260325, meeting.toString());
    }

    @Test
    public void equals() {
        LocalDate validDate1 = LocalDate.parse(VALID_DATE_20260325);
        LocalDate validDate2 = LocalDate.parse(VALID_DATE_20260401);

        Meeting meeting1 = new Meeting(VALID_DESCRIPTION_PROJECT, validDate1);
        Meeting meeting2 = new Meeting(VALID_DESCRIPTION_PROJECT, validDate1);
        Meeting meetingDiffDescription = new Meeting(VALID_DESCRIPTION_TEAM, validDate1);
        Meeting meetingDiffDate = new Meeting(VALID_DESCRIPTION_PROJECT, validDate2);

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
