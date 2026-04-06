package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.TagUtil.amendTagsOfPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnstarCommand}.
 */
public class UnstarCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unstarValidIndexUnfilteredList_success() {
        Person unstarredPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));
        UnstarCommand unstarCommand = new UnstarCommand(targetIndices);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> tagsCopy = new HashSet<>(unstarredPerson.getTags());
        Set<Tag> starTags = new HashSet<>(tagsCopy);
        starTags.add(new Tag(Tag.STAR_TAG));
        Person starredPerson = amendTagsOfPerson(unstarredPerson, starTags);

        // both models start with the starred person
        model.setPerson(unstarredPerson, starredPerson);
        expectedModel.setPerson(unstarredPerson, starredPerson);

        // expected model after unstar: goes back to the original
        expectedModel.setPerson(starredPerson, unstarredPerson);

        String expectedMessage = String.format(UnstarCommand.MESSAGE_UNSTAR_PERSON_SUCCESS,
                unstarredPerson.getName().fullName);

        assertCommandSuccess(unstarCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(outOfBoundIndex);
        UnstarCommand unstarCommand = new UnstarCommand(targetIndices);

        assertCommandFailure(unstarCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_unstarValidIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person unstarredPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));

        UnstarCommand unstarCommand = new UnstarCommand(targetIndices);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> tagsCopy = new HashSet<>(unstarredPerson.getTags());
        Set<Tag> starTags = new HashSet<>(tagsCopy);
        starTags.add(new Tag(Tag.STAR_TAG));
        Person starredPerson = amendTagsOfPerson(unstarredPerson, starTags);

        // both models start with the starred person
        model.setPerson(unstarredPerson, starredPerson);
        expectedModel.setPerson(unstarredPerson, starredPerson);

        // expected model after unstar: goes back to the original
        expectedModel.setPerson(starredPerson, unstarredPerson);

        String expectedMessage = String.format(UnstarCommand.MESSAGE_UNSTAR_PERSON_SUCCESS,
                unstarredPerson.getName().fullName);

        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(unstarCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(outOfBoundIndex);

        UnstarCommand unstarCommand = new UnstarCommand(targetIndices);

        assertCommandFailure(unstarCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Index> targetIndices1 = new HashSet<>();
        targetIndices1.add(INDEX_FIRST_PERSON);
        Set<Index> targetIndices2 = new HashSet<>();
        UnstarCommand unstarFirstCommand = new UnstarCommand(targetIndices1);
        UnstarCommand unstarSecondCommand = new UnstarCommand(targetIndices2);

        // same object -> returns true
        assertTrue(unstarFirstCommand.equals(unstarFirstCommand));

        // same values -> returns true
        UnstarCommand unstarFirstCommandCopy = new UnstarCommand(targetIndices1);
        assertTrue(unstarFirstCommand.equals(unstarFirstCommandCopy));

        // different types -> returns false
        assertFalse(unstarFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unstarFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unstarFirstCommand.equals(unstarSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));
        UnstarCommand unstarCommand = new UnstarCommand(targetIndices);
        String expected = UnstarCommand.class.getCanonicalName() + "{targetIndices=" + targetIndices + "}";
        assertEquals(expected, unstarCommand.toString());
    }

}
