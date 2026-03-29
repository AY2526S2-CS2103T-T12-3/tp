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
import static seedu.address.testutil.TypicalPersons.UUID_1;
import static seedu.address.testutil.TypicalPersons.UUID_2;
import static seedu.address.testutil.TypicalPersons.UUID_3;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class EditMeetingDescriptorTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(COFFEE_MEETING.getDescription());
        descriptor.setDate(COFFEE_MEETING.getDate());
        descriptor.setParticipantsID(Set.of(UUID_1, UUID_3));
        descriptor.setPeopleIndicesToAdd(Set.of(INDEX_SECOND_PERSON));
        descriptor.setPeopleIndicesToDelete(Set.of(INDEX_FIRST_PERSON));
        descriptor.setPeopleToAddId(Set.of(UUID_2));
        descriptor.setPeopleToDeleteId(Set.of(UUID_1));

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
        different.setParticipantsID(Set.of(UUID.randomUUID()));
        assertFalse(descriptor.equals(different));

        // different indices to add -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setPeopleIndicesToAdd(Set.of(INDEX_THIRD_PERSON));
        assertFalse(descriptor.equals(different));

        // different indices to delete -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setPeopleIndicesToDelete(Set.of(INDEX_SECOND_PERSON));
        assertFalse(descriptor.equals(different));

        // different peopleToAddId -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setPeopleToAddId(Set.of(UUID.randomUUID()));
        assertFalse(descriptor.equals(different));

        // different peopleToDeleteId -> returns false
        different = new EditMeetingDescriptor(descriptor);
        different.setPeopleToDeleteId(Set.of(UUID.randomUUID()));
        assertFalse(descriptor.equals(different));
    }

    @Test
    public void toStringMethod() {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        String expected = EditMeetingDescriptor.class.getCanonicalName() + "{description="
                + descriptor.getDescription().orElse(null) + ", date="
                + descriptor.getDate().orElse(null) + ", participantsID="
                + descriptor.getParticipantsID().orElse(null) + ", peopleIndicesToAdd="
                + descriptor.getPeopleIndicesToAdd().orElse(null) + ", peopleIndicesToDelete="
                + descriptor.getPeopleIndicesToDelete().orElse(null) + ", peopleToAddId="
                + descriptor.getPeopleToAddId().orElse(null) + ", peopleToDeleteId="
                + descriptor.getPeopleToDeleteId().orElse(null) + "}";
        assertEquals(expected, descriptor.toString());
    }

    @Test
    public void resolveParticipantIds_validIndices_success() throws Exception {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setPeopleIndicesToAdd(Set.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        descriptor.setPeopleIndicesToDelete(Set.of(INDEX_THIRD_PERSON));

        descriptor.resolveParticipantIds(model);

        // Verify that the converted IDs match the IDs of persons in model
        Set<UUID> expectedAddIds = Set.of(UUID_1, UUID_2);

        Set<UUID> expectedDeleteIds = Set.of(UUID_3);

        assertEquals(expectedAddIds, descriptor.getPeopleToAddId().orElseThrow());
        assertEquals(expectedDeleteIds, descriptor.getPeopleToDeleteId().orElseThrow());
    }

    @Test
    public void resolveParticipantIds_invalidIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setPeopleIndicesToAdd(Set.of(invalidIndex));
        assertThrows(CommandException.class, () -> descriptor.resolveParticipantIds(model));
    }
}
