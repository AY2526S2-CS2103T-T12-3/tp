package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_blank_throwsIllegalArgumentException() {
        String blankDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new Description(blankDescription));

        String spacesOnly = "   ";
        assertThrows(IllegalArgumentException.class, () -> new Description(spacesOnly));
    }

    @Test
    public void isValidDescription() {
        // null should throw
        assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid descriptions
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription("    ")); // spaces only

        // valid descriptions
        assertTrue(Description.isValidDescription(VALID_DESCRIPTION_PROJECT)); // normal text
        assertTrue(Description.isValidDescription("12345")); // numbers
        assertTrue(Description.isValidDescription("Discuss Q1 goals & budget")); // alphanumeric with symbols
        assertTrue(Description.isValidDescription(VALID_DESCRIPTION_TEAM)); // capital letters
    }

    @Test
    public void equals() {
        Description desc = new Description(VALID_DESCRIPTION_PROJECT);

        // same values -> true
        assertTrue(desc.equals(new Description(VALID_DESCRIPTION_PROJECT)));

        // same object -> true
        assertTrue(desc.equals(desc));

        // null -> false
        assertFalse(desc.equals(null));

        // different types -> false
        assertFalse(desc.equals(5.0f));

        // different values -> false
        assertFalse(desc.equals(new Description(VALID_DESCRIPTION_TEAM)));
    }

    @Test
    public void toString_returnsDescription() {
        Description desc = new Description(VALID_DESCRIPTION_PROJECT);
        assertTrue(desc.toString().equals(VALID_DESCRIPTION_PROJECT));
    }
}
