package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SINGLE;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDICES_MULTIPLE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.DeleteMeetingCommand.MESSAGE_INVALID_MEETING_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;

public class DeleteMeetingCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_singleIndex_success() throws Exception {
        List<Meeting> lastSeenList = model.getFilteredMeetingList();

        List<Meeting> meetingToDelete = List.of(lastSeenList.get(INDEX_FIRST_PERSON.getZeroBased()));

        // Execute DeleteMeetingCommand
        DeleteMeetingCommand command = new DeleteMeetingCommand(VALID_INDEX_SINGLE);

        CommandResult response = command.execute(model);

        // Expected message
        String expectedMessage = String.format(DeleteMeetingCommand.MESSAGE_DELETE_MEETING_SUCCESS, meetingToDelete);

        assertEquals(expectedMessage, response.getFeedbackToUser());

        // Check that the meeting was deleted
        assertFalse(model.hasMeeting(meetingToDelete.get(0)));
    }

    @Test
    public void execute_multipleIndices_success() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Meeting> lastSeenList = model.getFilteredMeetingList();

        // Note: Typical Address Book must have at least 2 meetings.
        List<Meeting> meetingsToDelete = List.of(
                lastSeenList.get(INDEX_FIRST_PERSON.getZeroBased()),
                lastSeenList.get(INDEX_SECOND_PERSON.getZeroBased())
        );

        // Execute DeleteMeetingCommand
        DeleteMeetingCommand command = new DeleteMeetingCommand(Set.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        CommandResult response = command.execute(model);

        // Expected message
        String expectedMessage = String.format(DeleteMeetingCommand.MESSAGE_DELETE_MEETING_SUCCESS, meetingsToDelete);

        assertEquals(expectedMessage, response.getFeedbackToUser());

        // Check that the meeting was deleted
        meetingsToDelete.forEach(m -> assertFalse(model.hasMeeting(m)));
    }

    @Test
    public void execute_invalidIndexOutOfBounds_throwsCommandException() {
        int outOfBounds = model.getFilteredPersonList().size();
        Index invalidIndex = Index.fromOneBased(outOfBounds);

        DeleteMeetingCommand command = new DeleteMeetingCommand(Set.of(invalidIndex));

        assertCommandFailure(command, model, String.format(
                MESSAGE_INVALID_MEETING_INDEX,
                outOfBounds));
    }

    @Test
    public void equals() {
        DeleteMeetingCommand firstCommand = new DeleteMeetingCommand(Set.of(Index.fromOneBased(1)));
        DeleteMeetingCommand secondCommand = new DeleteMeetingCommand(VALID_INDICES_MULTIPLE);

        // same object
        assertEquals(firstCommand, firstCommand);

        // same values
        DeleteMeetingCommand firstCommandCopy = new DeleteMeetingCommand(Set.of(Index.fromOneBased(1)));
        assertEquals(firstCommand, firstCommandCopy);

        // different types
        assertTrue(!firstCommand.equals(1));

        // null
        assertTrue(!firstCommand.equals(null));

        // different values
        assertTrue(!firstCommand.equals(secondCommand));
    }
}
