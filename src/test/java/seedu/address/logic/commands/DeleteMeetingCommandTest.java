package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SINGLE;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDICES_MULTIPLE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.DeleteMeetingCommand.MESSAGE_INVALID_MEETING_INDEX;
import static seedu.address.logic.commands.MeetingUtil.createPersonWithGivenMeetings;
import static seedu.address.logic.commands.MeetingUtil.createPersonWithMeetingAdded;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDEX_SINGLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDICES_MULTIPLE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

public class DeleteMeetingCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_singleIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<UUID> participantIds = List.of(firstPerson.getId());
        Meeting meetingToAdd = new Meeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260325, participantIds);

        model.setPerson(firstPerson, createPersonWithMeetingAdded(firstPerson, meetingToAdd));

        // Execute DeleteMeetingCommand
        DeleteMeetingCommand command = new DeleteMeetingCommand(VALID_INDEX_SINGLE);

        CommandResult response = command.execute(model);

        // Expected message
        String expectedMessage = String.format(
                DeleteMeetingCommand.MESSAGE_DELETE_MEETING_SUCCESS, INPUT_INDEX_SINGLE.trim()
        );

        assertEquals(expectedMessage, response.getFeedbackToUser());

        // Check that the meeting was deleted
        Person updatedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(updatedPerson.getMeetings().isEmpty());
    }

    @Test
    public void execute_multipleIndices_success() throws Exception {
        List<Person> persons = model.getFilteredPersonList().subList(0, 3);
        List<UUID> participantIds = persons.stream().map(Person::getId).toList();

        Meeting testMeeting1 = new Meeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260325, participantIds);
        Meeting testMeeting2 = new Meeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260401, participantIds);
        Meeting testMeeting3 = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325, participantIds);

        Set<Meeting> setOfMeetings = Set.of(testMeeting1, testMeeting2, testMeeting3);
        for (Person p : persons) {
            model.setPerson(p, createPersonWithGivenMeetings(p, setOfMeetings));
        }

        // Execute DeleteMeetingCommand for all three meetings
        DeleteMeetingCommand command = new DeleteMeetingCommand(VALID_INDICES_MULTIPLE);

        CommandResult response = command.execute(model);

        // Expected message (only the indices that existed)
        String expectedMessage = String.format(
                DeleteMeetingCommand.MESSAGE_DELETE_MEETING_SUCCESS, INPUT_INDICES_MULTIPLE.trim()
        );
        assertEquals(expectedMessage, response.getFeedbackToUser());

        // Check all meetings removed
        for (int i = 0; i < 3; i++) {
            Person updatedPerson = model.getFilteredPersonList().get(i);
            assertTrue(updatedPerson.getMeetings().isEmpty());
        }
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
