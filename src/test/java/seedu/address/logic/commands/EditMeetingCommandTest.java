package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalMeetings.COFFEE_MEETING;
import static seedu.address.testutil.TypicalMeetings.PROJECT_MEETING;
import static seedu.address.testutil.TypicalMeetings.STRATEGY_MEETING;
import static seedu.address.testutil.TypicalPersons.UUID_1;
import static seedu.address.testutil.TypicalPersons.UUID_3;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;

public class EditMeetingCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        Meeting meetingToEdit = model.getFilteredMeetingList().get(targetIndex.getZeroBased());

        String newDescription = COFFEE_MEETING.getDescription();
        LocalDate newDate = STRATEGY_MEETING.getDate();

        Set<UUID> expectedParticipants = new HashSet<>(meetingToEdit.getParticipantsID());
        expectedParticipants.add(UUID_3);
        expectedParticipants.remove(UUID_1);

        Meeting editedMeeting = new Meeting(newDescription, newDate, expectedParticipants);

        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(newDescription);
        descriptor.setDate(newDate);
        descriptor.setPeopleIndicesToAdd(Set.of(INDEX_THIRD_PERSON));
        descriptor.setPeopleIndicesToDelete(Set.of(INDEX_FIRST_PERSON));

        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(Set.of(targetIndex), descriptor);

        String expectedMessage = String.format(EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS, "1");

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setMeeting(meetingToEdit, editedMeeting);

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        Meeting meetingToEdit = model.getFilteredMeetingList().get(targetIndex.getZeroBased());

        String newDescription = STRATEGY_MEETING.getDescription();

        Meeting editedMeeting = new Meeting(newDescription,
                meetingToEdit.getDate(),
                meetingToEdit.getParticipantsID());

        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(newDescription);

        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(Set.of(targetIndex), descriptor);

        String expectedMessage = String.format(EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS, "2");

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setMeeting(meetingToEdit, editedMeeting);

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(
                Set.of(INDEX_FIRST_PERSON),
                new EditMeetingDescriptor());
        String expectedMessage = String.format(EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS, "1");

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidMeetingIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredMeetingList().size() + 1);
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(PROJECT_MEETING.getDescription());

        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(Set.of(outOfBoundIndex), descriptor);

        assertCommandFailure(editMeetingCommand, model,
                String.format(seedu.address.logic.commands.DeleteMeetingCommand.MESSAGE_INVALID_MEETING_INDEX,
                        outOfBoundIndex.getOneBased()));
    }

    @Test
    public void equals() {
        Set<Index> indices = Set.of(Index.fromOneBased(1));
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(PROJECT_MEETING.getDescription());

        final EditMeetingCommand standardCommand = new EditMeetingCommand(indices, descriptor);

        // same values -> returns true
        EditMeetingDescriptor copyDescriptor = new EditMeetingDescriptor(descriptor);
        EditMeetingCommand commandWithSameValues = new EditMeetingCommand(indices, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditMeetingCommand(Set.of(Index.fromOneBased(2)), descriptor)));

        // different descriptor -> returns false
        EditMeetingDescriptor differentDescriptor = new EditMeetingDescriptor();
        differentDescriptor.setDescription(COFFEE_MEETING.getDescription());
        assertFalse(standardCommand.equals(new EditMeetingCommand(indices, differentDescriptor)));
    }

    @Test
    public void toStringMethod() {
        Set<Index> indices = Set.of(INDEX_FIRST_PERSON);
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(indices, descriptor);

        // Expected string using ToStringBuilder style
        String expected = new ToStringBuilder(editMeetingCommand)
                .add("meetingIndices", indices)
                .add("editMeetingDescriptor", descriptor)
                .toString();

        assertEquals(expected, editMeetingCommand.toString());
    }
}
