package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_NON_EXISTENT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_WRONG_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INDEX_SINGLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INDICES_MULTIPLE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_20260325;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_20260401;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TEAM;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class AddMeetingCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_singleIndex_success() throws Exception {
        Set<Index> indices = VALID_INDEX_SINGLE;
        AddMeetingCommand command = new AddMeetingCommand(indices, VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);

        Person targetPerson = model.getFilteredPersonList().get(indices.iterator().next().getZeroBased());

        String expectedMessage = String.format(AddMeetingCommand.MESSAGE_ADD_MEETING_SUCCESS,
                targetPerson.getName().fullName);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(targetPerson, targetPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndices_success() throws Exception {
        AddMeetingCommand command = new AddMeetingCommand(VALID_INDICES_MULTIPLE, VALID_DESCRIPTION_TEAM, VALID_DATE_20260401);

        // Directly use the Person constants in the expected message
        String expectedMessage = String.format(AddMeetingCommand.MESSAGE_ADD_MEETING_SUCCESS,
                String.join(", ",
                        ALICE.getName().fullName,
                        BENSON.getName().fullName,
                        CARL.getName().fullName));

        // Build expected model by adding meetings to each person
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(ALICE, ALICE);
        expectedModel.setPerson(BENSON, BENSON);
        expectedModel.setPerson(CARL, CARL);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexOutOfBounds_throwsCommandException() {
        // Compute an index outside the current person list
        int outOfBounds = model.getFilteredPersonList().size() + 1;
        Index outOfBoundsIndex = Index.fromOneBased(outOfBounds);
        Set<Index> outOfBoundsIndices = Set.of(outOfBoundsIndex);

        AddMeetingCommand command = new AddMeetingCommand(outOfBoundsIndices, VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);

        assertCommandFailure(command, model, AddMeetingCommand.MESSAGE_INVALID_PERSON_INDEX);
    }

    @Test
    public void execute_invalidDateFormat_throwsCommandException() {
        Set<Index> indices = VALID_INDEX_SINGLE;
        AddMeetingCommand commandWrongFormat = new AddMeetingCommand(indices, VALID_DESCRIPTION_PROJECT, INVALID_DATE_WRONG_FORMAT);
        AddMeetingCommand commandNonExistent = new AddMeetingCommand(indices, VALID_DESCRIPTION_PROJECT, INVALID_DATE_NON_EXISTENT);

        assertCommandFailure(commandWrongFormat, model, AddMeetingCommand.MESSAGE_INVALID_DATE_FORMAT);
        assertCommandFailure(commandNonExistent, model, AddMeetingCommand.MESSAGE_INVALID_DATE_FORMAT);
    }

    @Test
    public void equals() {
        Set<Index> firstIndices = VALID_INDEX_SINGLE;
        Set<Index> secondIndices = VALID_INDICES_MULTIPLE;

        AddMeetingCommand firstCommand = new AddMeetingCommand(firstIndices, VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);
        AddMeetingCommand secondCommand = new AddMeetingCommand(secondIndices, VALID_DESCRIPTION_TEAM, VALID_DATE_20260401);

        // same object -> returns true
        assertEquals(firstCommand, firstCommand);

        // same values -> returns true
        AddMeetingCommand firstCommandCopy = new AddMeetingCommand(firstIndices, VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);
        assertEquals(firstCommand, firstCommandCopy);

        // different types -> returns false
        assertEquals(false, firstCommand.equals(1));

        // null -> returns false
        assertEquals(false, firstCommand.equals(null));

        // different values -> returns false
        assertEquals(false, firstCommand.equals(secondCommand));
    }
}
