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
import static seedu.address.logic.commands.MeetingUtil.createPersonWithGivenMeetings;
import static seedu.address.logic.commands.MeetingUtil.createPersonWithMeetingAdded;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
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
        // Setup: get first person and add a meeting

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<UUID> participantIds = List.of(firstPerson.getId());
        Meeting meetingToAdd = new Meeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260325, participantIds);

        model.setPerson(firstPerson, createPersonWithMeetingAdded(firstPerson, meetingToAdd));

        // Execute DeleteMeetingCommand
        DeleteMeetingCommand command = new DeleteMeetingCommand(
                VALID_INDEX_SINGLE, VALID_INDEX_SINGLE
        );

        CommandResult response = command.execute(model);

        // Check that the meeting was deleted
        Person updatedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(updatedPerson.getMeetings().isEmpty());
    }

    @Test
    public void execute_multipleIndices_success() throws Exception {
        // Setup: add the same meeting to first three persons
        List<Person> persons = model.getFilteredPersonList().subList(0, 3);
        List<UUID> participantIds = persons.stream().map(Person::getId).toList();
        Meeting testMeeting1 = new Meeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260325, participantIds);
        Meeting testMeeting2 = new Meeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260401, participantIds);
        Meeting testMeeting3 = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325, participantIds);

        Set<Meeting> setOfMeetings = Set.of(testMeeting1, testMeeting2, testMeeting3);
        for (Person p : persons) {
            model.setPerson(p, createPersonWithGivenMeetings(p, setOfMeetings));
        }

        // Execute DeleteMeetingCommand for all three persons
        DeleteMeetingCommand command = new DeleteMeetingCommand(
                Set.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON),
                VALID_INDEX_SINGLE
        );

        CommandResult response = command.execute(model);

        // Check all meetings removed
        for (int i = 0; i < 3; i++) {
            Person updatedPerson = model.getFilteredPersonList().get(i);
            assertTrue(updatedPerson.getMeetings().isEmpty());
        }
    }

    @Test
    public void execute_invalidIndexOutOfBounds_throwsCommandException() {
        int outOfBounds = model.getFilteredPersonList().size() + 1;
        Index invalidIndex = Index.fromOneBased(outOfBounds);

        DeleteMeetingCommand command = new DeleteMeetingCommand(
                Set.of(invalidIndex),
                Set.of(Index.fromOneBased(1))
        );

        assertCommandFailure(command, model, AddMeetingCommand.MESSAGE_INVALID_PERSON_INDEX);
    }

    @Test
    public void equals() {
        DeleteMeetingCommand firstCommand = new DeleteMeetingCommand(
                Set.of(Index.fromOneBased(1)), Set.of(Index.fromOneBased(1))
        );

        DeleteMeetingCommand secondCommand = new DeleteMeetingCommand(
                VALID_INDICES_MULTIPLE, Set.of(Index.fromOneBased(1))
        );

        // same object
        assertEquals(firstCommand, firstCommand);

        // same values
        DeleteMeetingCommand firstCommandCopy =
                new DeleteMeetingCommand(Set.of(Index.fromOneBased(1)), Set.of(Index.fromOneBased(1)));
        assertEquals(firstCommand, firstCommandCopy);

        // different types
        assertEquals(false, firstCommand.equals(1));

        // null
        assertEquals(false, firstCommand.equals(null));

        // different values
        assertEquals(false, firstCommand.equals(secondCommand));
    }
}
