package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ID_1;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class PersonIdTest {

    @Test
    public void constructor_null_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PersonId(null));
    }

    @Test
    public void constructor_blank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PersonId(""));
        assertThrows(IllegalArgumentException.class, () -> new PersonId("   "));
    }

    @Test
    public void constructor_invalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PersonId(VALID_DESCRIPTION_PROJECT));
        assertThrows(IllegalArgumentException.class, () -> new PersonId(VALID_DATE_20260325));
    }

    @Test
    public void constructor_validId_success() {
        PersonId id = new PersonId(ID_1);
        assertTrue(id.getUuid().equals(UUID.fromString(ID_1)));
        assertTrue(id.toString().equals(ID_1));
    }

    @Test
    public void constructor_noArgs_generatesRandomId() {
        PersonId id1 = new PersonId();
        PersonId id2 = new PersonId();
        // not null
        assertTrue(id1.getUuid() != null);
        assertTrue(id2.getUuid() != null);
        // very low chance of equality
        assertFalse(id1.equals(id2));
    }

    @Test
    public void equals() {
        PersonId id1 = new PersonId(ID_1);
        PersonId id2 = new PersonId(ID_1);
        PersonId id3 = new PersonId(); // random

        // same values -> true
        assertTrue(id1.equals(id2));

        // same object -> true
        assertTrue(id1.equals(id1));

        // null -> false
        assertFalse(id1.equals(null));

        // different type -> false
        assertFalse(id1.equals("some string"));

        // different values -> false
        assertFalse(id1.equals(id3));
    }
}
