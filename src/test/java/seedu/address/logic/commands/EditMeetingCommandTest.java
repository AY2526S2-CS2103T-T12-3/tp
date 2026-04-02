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
import static seedu.address.testutil.TypicalPersons.ID_1;
import static seedu.address.testutil.TypicalPersons.ID_3;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.person.PersonId;

public class EditMeetingCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        Meeting meetingToEdit = model.getFilteredMeetingList().get(targetIndex.getZeroBased());

        Description newDescription = COFFEE_MEETING.getDescription();
        MeetingDate newDate = STRATEGY_MEETING.getDate();

        Set<PersonId> expectedParticipants = new HashSet<>(meetingToEdit.getParticipantsIDs());
        expectedParticipants.add(new PersonId(ID_3));
        expectedParticipants.remove(new PersonId(ID_1));

        Meeting editedMeeting = new Meeting(newDescription, newDate, expectedParticipants);

        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(newDescription);
        descriptor.setDate(newDate);
        descriptor.setPersonIndicesToAdd(Set.of(INDEX_THIRD_PERSON));
        descriptor.setPersonIndicesToDelete(Set.of(INDEX_FIRST_PERSON));

        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS, "1");

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setMeeting(meetingToEdit, editedMeeting);

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Meeting meetingToEdit = model.getFilteredMeetingList().get(INDEX_SECOND_PERSON.getZeroBased());

        Description newDescription = STRATEGY_MEETING.getDescription();

        Meeting editedMeeting = new Meeting(newDescription,
                meetingToEdit.getDate(),
                meetingToEdit.getParticipantsIDs());

        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(newDescription);

        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(INDEX_SECOND_PERSON, descriptor);

        String expectedMessage = String.format(
                EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS,
                INDEX_SECOND_PERSON.getOneBased());

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setMeeting(meetingToEdit, editedMeeting);

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(
                INDEX_FIRST_PERSON,
                new EditMeetingDescriptor());
        String expectedMessage = String.format(
                EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS,
                INDEX_FIRST_PERSON.getOneBased());

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidMeetingIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredMeetingList().size() + 1);
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(PROJECT_MEETING.getDescription());

        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editMeetingCommand, model,
                String.format(seedu.address.logic.commands.DeleteMeetingCommand.MESSAGE_INVALID_MEETING_INDEX,
                        outOfBoundIndex.getOneBased()));
    }

    @Test
    public void equals() {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(PROJECT_MEETING.getDescription());

        final EditMeetingCommand standardCommand = new EditMeetingCommand(INDEX_FIRST_PERSON, descriptor);

        // same values -> returns true
        EditMeetingDescriptor copyDescriptor = new EditMeetingDescriptor(descriptor);
        EditMeetingCommand commandWithSameValues = new EditMeetingCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditMeetingCommand(INDEX_SECOND_PERSON, descriptor)));

        // different descriptor -> returns false
        EditMeetingDescriptor differentDescriptor = new EditMeetingDescriptor();
        differentDescriptor.setDescription(COFFEE_MEETING.getDescription());
        assertFalse(standardCommand.equals(new EditMeetingCommand(INDEX_FIRST_PERSON, differentDescriptor)));
    }

    @Test
    public void toStringMethod() {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(INDEX_FIRST_PERSON, descriptor);

        // Expected string using ToStringBuilder style
        String expected = new ToStringBuilder(editMeetingCommand)
                .add("meetingIndex", INDEX_FIRST_PERSON)
                .add("editMeetingDescriptor", descriptor)
                .toString();

        assertEquals(expected, editMeetingCommand.toString());
    }
}
