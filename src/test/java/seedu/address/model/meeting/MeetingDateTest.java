package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DATE_NON_EXISTENT;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DATE_WRONG_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class MeetingDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MeetingDate(null));
    }

    @Test
    public void constructor_invalidFormat_throwsIllegalArgumentException() {
        // empty string
        assertThrows(IllegalArgumentException.class, () -> new MeetingDate(""));
        // spaces only
        assertThrows(IllegalArgumentException.class, () -> new MeetingDate("   "));
        // invalid date
        assertThrows(IllegalArgumentException.class, () -> new MeetingDate(INVALID_DATE_NON_EXISTENT));
        // wrong format
        assertThrows(IllegalArgumentException.class, () -> new MeetingDate(INVALID_DATE_WRONG_FORMAT));
    }

    @Test
    public void constructor_validDate_success() {
        MeetingDate date = new MeetingDate(VALID_DATE_20270325);
        assertEquals(LocalDate.parse(VALID_DATE_20270325), date.getDate());
        assertEquals(VALID_DATE_20270325, date.toString());
    }

    @Test
    public void isValidDateString() {
        // null should throw
        assertThrows(NullPointerException.class, () -> MeetingDate.isValidDateString(null));

        // invalid
        assertFalse(MeetingDate.isValidDateString("")); // empty
        assertFalse(MeetingDate.isValidDateString(VALID_DESCRIPTION_TEAM)); // not a date
        assertFalse(MeetingDate.isValidDateString(INVALID_DATE_NON_EXISTENT)); // invalid calendar date

        // valid
        assertTrue(MeetingDate.isValidDateString(VALID_DATE_20270325));
        assertTrue(MeetingDate.isValidDateString("2024-02-29")); // leap year
    }

    @Test
    public void comparisons() {
        MeetingDate date1 = new MeetingDate(VALID_DATE_20270325);
        MeetingDate date2 = new MeetingDate(VALID_DATE_20270401);

        assertTrue(date1.isBefore(date2.getDate()));
        assertTrue(date2.isAfter(date1.getDate()));

        assertFalse(date1.isAfter(date2.getDate()));
        assertFalse(date2.isBefore(date1.getDate()));

        assertTrue(date1.compareTo(date2) < 0);
    }

    @Test
    public void equalsAndHashCode() {
        MeetingDate md1 = new MeetingDate(VALID_DATE_20270325);
        MeetingDate md2 = new MeetingDate(VALID_DATE_20270325);
        MeetingDate md3 = new MeetingDate(VALID_DATE_20270401);

        // equals
        assertTrue(md1.equals(md1)); // same object
        assertTrue(md1.equals(md2)); // same date
        assertFalse(md1.equals(md3)); // different date
        assertFalse(md1.equals(null));
        assertFalse(md1.equals(VALID_DATE_20270401)); // different type

        // hashCode
        assertEquals(md1.hashCode(), md2.hashCode());
        assertNotEquals(md1.hashCode(), md3.hashCode());
    }
}
