package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;


public class DeleteTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noTagsAdded_failure() {
        Set<Tag> emptyTagSet = new HashSet<>();
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(targetIndices, emptyTagSet);

        assertCommandFailure(deleteTagCommand, model, DeleteTagCommand.MESSAGE_NO_TAGS);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        List<Index> targetIndices = new ArrayList<>();
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        targetIndices.add(outOfBoundIndex);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("school")); // the error message should return as long as any tag is included

        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(targetIndices, tags);

        assertCommandFailure(deleteTagCommand, model, "Error: " + Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_allInvalidTags_throwsCommandException() {
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("lllllllllllllllllllllllll"));
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(targetIndices, tags);

        assertCommandFailure(deleteTagCommand,
                model,
                "Error: None of the specified tags exist in any of the specified contacts.");
    }

    @Test
    public void execute_validIndexValidTags_success() {
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        Person personToDeleteFrom = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tags = personToDeleteFrom.getTags();
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(targetIndices, tags);

        String expectedMessage = String.format(MESSAGE_DELETE_TAG_SUCCESS, tags.toString());

        Person editedPerson = new Person(
                personToDeleteFrom.getName(),
                personToDeleteFrom.getPhone(),
                personToDeleteFrom.getEmail(),
                new HashSet<>()
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToDeleteFrom, editedPerson);

        assertCommandSuccess(deleteTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        List<Index> targetIndices1 = new ArrayList<>();
        List<Index> targetIndices2 = new ArrayList<>();
        Set<Tag> tags = new HashSet<>();
        targetIndices1.add(INDEX_FIRST_PERSON);
        targetIndices2.add(INDEX_SECOND_PERSON);
        DeleteTagCommand deleteTagFirstCommand = new DeleteTagCommand(targetIndices1, tags);
        DeleteTagCommand deleteTagSecondCommand = new DeleteTagCommand(targetIndices2, tags);

        // same object -> returns true
        assertTrue(deleteTagFirstCommand.equals(deleteTagFirstCommand));

        // same values -> returns true
        DeleteTagCommand deleteFirstCommandCopy = new DeleteTagCommand(targetIndices1, tags);
        assertTrue(deleteTagFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteTagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteTagFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteTagFirstCommand.equals(deleteTagSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(targetIndex);
        Set<Tag> tags = new HashSet<>();
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(targetIndices, tags);
        String expected = DeleteTagCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteTagCommand.toString());
    }
}
