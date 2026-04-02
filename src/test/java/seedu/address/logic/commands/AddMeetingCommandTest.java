package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.person.Person;
import seedu.address.testutil.MeetingBuilder;

public class AddMeetingCommandTest {
    public static final String VALID_DESCRIPTION_PROJECT = "Project discussion";
    public static final String VALID_DESCRIPTION_TEAM = "Team Meeting";
    public static final String VALID_DATE_20260325 = "2026-03-25";
    public static final String VALID_DATE_20260401 = "2026-04-01";
    public static final Set<Index> VALID_INDEX_SINGLE = Set.of(INDEX_FIRST_PERSON);
    public static final Set<Index> VALID_INDICES_MULTIPLE = Set.of(
            INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON
    );

    public static final String INVALID_DESCRIPTION = "";
    public static final String INVALID_DATE_WRONG_FORMAT = "25-03-2026";
    public static final String INVALID_DATE_NON_EXISTENT = "2026-02-30";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_singleIndex_success() throws Exception {
        Set<Index> indices = VALID_INDEX_SINGLE;
        AddMeetingCommand command = new AddMeetingCommand(indices,
                new Description(VALID_DESCRIPTION_PROJECT),
                new MeetingDate(VALID_DATE_20260325));

        // Original target person
        Person targetPerson = model.getFilteredPersonList()
                .get(indices.iterator().next().getZeroBased());

        // Collect participant IDs for the meeting
        Set<String> participantIds = Set.of(targetPerson.getId().toString());

        // Meeting using MeetingBuilder
        Meeting meeting = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20260325)
                .withParticipants(participantIds)
                .build();

        String expectedMessage = String.format(AddMeetingCommand.MESSAGE_ADD_MEETING_SUCCESS, meeting);

        // Build expected model
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addMeeting(meeting);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndices_success() throws Exception {
        AddMeetingCommand command = new AddMeetingCommand(VALID_INDICES_MULTIPLE,
                new Description(VALID_DESCRIPTION_TEAM),
                new MeetingDate(VALID_DATE_20260401));

        // Collect participant IDs from all target persons
        Set<String> participantIds = Set.of(
                ALICE.getId().toString(),
                BENSON.getId().toString(),
                CARL.getId().toString()
        );

        // Meeting using MeetingBuilder
        Meeting meeting = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_TEAM)
                .withDate(VALID_DATE_20260401)
                .withParticipants(participantIds)
                .build();

        // Build expected model
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addMeeting(meeting);

        String expectedMessage = String.format(AddMeetingCommand.MESSAGE_ADD_MEETING_SUCCESS, meeting);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexOutOfBounds_throwsCommandException() {
        int outOfBounds = model.getFilteredPersonList().size() + 1;
        Index outOfBoundsIndex = Index.fromOneBased(outOfBounds);
        Set<Index> outOfBoundsIndices = Set.of(outOfBoundsIndex);

        AddMeetingCommand command = new AddMeetingCommand(outOfBoundsIndices,
                new Description(VALID_DESCRIPTION_PROJECT),
                new MeetingDate(VALID_DATE_20260325));

        assertCommandFailure(command, model, AddMeetingCommand.MESSAGE_INVALID_PERSON_INDEX);
    }

    @Test
    public void equals() {
        Set<Index> firstIndices = VALID_INDEX_SINGLE;
        Set<Index> secondIndices = VALID_INDICES_MULTIPLE;

        AddMeetingCommand firstCommand = new AddMeetingCommand(firstIndices,
                new Description(VALID_DESCRIPTION_PROJECT),
                new MeetingDate(VALID_DATE_20260325));
        AddMeetingCommand secondCommand = new AddMeetingCommand(secondIndices,
                new Description(VALID_DESCRIPTION_TEAM),
                new MeetingDate(VALID_DATE_20260401));

        // same object -> returns true
        assertEquals(firstCommand, firstCommand);

        // same values -> returns true
        AddMeetingCommand firstCommandCopy = new AddMeetingCommand(firstIndices,
                new Description(VALID_DESCRIPTION_PROJECT),
                new MeetingDate(VALID_DATE_20260325));
        assertEquals(firstCommand, firstCommandCopy);

        // different types -> returns false
        assertEquals(false, firstCommand.equals(1));

        // null -> returns false
        assertEquals(false, firstCommand.equals(null));

        // different values -> returns false
        assertEquals(false, firstCommand.equals(secondCommand));
    }
}
