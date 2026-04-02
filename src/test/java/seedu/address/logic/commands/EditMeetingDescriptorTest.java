package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalMeetings.COFFEE_MEETING;
import static seedu.address.testutil.TypicalMeetings.PROJECT_MEETING;
import static seedu.address.testutil.TypicalPersons.ID_1;
import static seedu.address.testutil.TypicalPersons.ID_2;
import static seedu.address.testutil.TypicalPersons.ID_3;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonId;

public class EditMeetingDescriptorTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(COFFEE_MEETING.getDescription());
        descriptor.setDate(COFFEE_MEETING.getDate());
        descriptor.setParticipantsID(Set.of(new PersonId(ID_1), new PersonId(ID_3)));
        descriptor.setPersonIndicesToAdd(Set.of(INDEX_SECOND_PERSON));
        descriptor.setPersonIndicesToDelete(Set.of(INDEX_FIRST_PERSON));
        descriptor.setIdsToAdd(Set.of(new PersonId(ID_2)));
        descriptor.setIdsToDelete(Set.of(new PersonId(ID_1)));

        // same values -> returns true
        EditMeetingDescriptor copy = new EditMeetingDescriptor(descriptor);
        assertTrue(descriptor.equals(copy));

        // same object -> returns true
        assertTrue(descriptor.equals(descriptor));

        // null -> returns false
        assertFalse(descriptor.equals(null));

        // different types -> returns false
        assertFalse(descriptor.equals(5));

        // different description -> returns false
        EditMeetingDescriptor different = new EditMeetingDescriptor(descriptor);
        different.setDescription(PROJECT_MEETING.getDescription());
        assertFalse(descriptor.equals(different));

        // different date -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setDate(PROJECT_MEETING.getDate());
        assertFalse(descriptor.equals(different));

        // different participants id -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setParticipantsID(Set.of(new PersonId()));
        assertFalse(descriptor.equals(different));

        // different indices to add -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setPersonIndicesToAdd(Set.of(INDEX_THIRD_PERSON));
        assertFalse(descriptor.equals(different));

        // different indices to delete -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setPersonIndicesToDelete(Set.of(INDEX_SECOND_PERSON));
        assertFalse(descriptor.equals(different));

        // different peopleToAddId -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setIdsToAdd(Set.of(new PersonId()));
        assertFalse(descriptor.equals(different));

        // different peopleToDeleteId -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setIdsToDelete(Set.of(new PersonId()));
        assertFalse(descriptor.equals(different));
    }

    @Test
    public void toStringMethod() {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        String expected = EditMeetingDescriptor.class.getCanonicalName() + "{description="
                + descriptor.getDescription().orElse(null) + ", date="
                + descriptor.getDate().orElse(null) + ", participantsID="
                + descriptor.getParticipantsID().orElse(null) + ", peopleIndicesToAdd="
                + descriptor.getPersonIndicesToAdd().orElse(null) + ", peopleIndicesToDelete="
                + descriptor.getPersonIndicesToDelete().orElse(null) + ", peopleToAddId="
                + descriptor.getIdsToAdd().orElse(null) + ", peopleToDeleteId="
                + descriptor.getIdsToDelete().orElse(null) + "}";
        assertEquals(expected, descriptor.toString());
    }

    @Test
    public void resolveParticipantIds_validIndices_success() throws Exception {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setPersonIndicesToAdd(Set.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        descriptor.setPersonIndicesToDelete(Set.of(INDEX_THIRD_PERSON));

        descriptor.resolveParticipantIds(model);

        // Verify that the converted IDs match the IDs of persons in model
        Set<PersonId> expectedAddIds = Set.of(new PersonId(ID_1), new PersonId(ID_2));
        Set<PersonId> expectedDeleteIds = Set.of(new PersonId(ID_3));

        assertEquals(expectedAddIds, descriptor.getIdsToAdd().orElseThrow());
        assertEquals(expectedDeleteIds, descriptor.getIdsToDelete().orElseThrow());
    }

    @Test
    public void resolveParticipantIds_invalidIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setPersonIndicesToAdd(Set.of(invalidIndex));
        assertThrows(CommandException.class, () -> descriptor.resolveParticipantIds(model));
    }
}
