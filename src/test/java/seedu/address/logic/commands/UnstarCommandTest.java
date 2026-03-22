package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
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
        UnstarCommand unstarCommand = new UnstarCommand(INDEX_FIRST_PERSON);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> tagsCopy = new HashSet<>(unstarredPerson.getTags());
        Set<Tag> starTags = new HashSet<>(tagsCopy);
        starTags.add(new Tag(Tag.STAR_TAG));
        Person starredPerson = new Person(
                unstarredPerson.getName(),
                unstarredPerson.getPhone(),
                unstarredPerson.getEmail(),
                starTags,
                unstarredPerson.getMeetings());

        // both models start with the starred person
        model.setPerson(unstarredPerson, starredPerson);
        expectedModel.setPerson(unstarredPerson, starredPerson);

        // expected model after unstar: goes back to the original
        expectedModel.setPerson(starredPerson, unstarredPerson);

        String expectedMessage = String.format(UnstarCommand.MESSAGE_UNSTAR_PERSON_SUCCESS,
                Messages.format(unstarredPerson));

        assertCommandSuccess(unstarCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnstarCommand unstarCommand = new UnstarCommand(outOfBoundIndex);

        assertCommandFailure(unstarCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_unstarValidIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person unstarredPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnstarCommand unstarCommand = new UnstarCommand(INDEX_FIRST_PERSON);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> tagsCopy = new HashSet<>(unstarredPerson.getTags());
        Set<Tag> starTags = new HashSet<>(tagsCopy);
        starTags.add(new Tag(Tag.STAR_TAG));
        Person starredPerson = new Person(
                unstarredPerson.getName(),
                unstarredPerson.getPhone(),
                unstarredPerson.getEmail(),
                starTags,
                unstarredPerson.getMeetings());

        // both models start with the starred person
        model.setPerson(unstarredPerson, starredPerson);
        expectedModel.setPerson(unstarredPerson, starredPerson);

        // expected model after unstar: goes back to the original
        expectedModel.setPerson(starredPerson, unstarredPerson);

        String expectedMessage = String.format(UnstarCommand.MESSAGE_UNSTAR_PERSON_SUCCESS,
                Messages.format(unstarredPerson));

        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(unstarCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        UnstarCommand unstarCommand = new UnstarCommand(outOfBoundIndex);

        assertCommandFailure(unstarCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnstarCommand starFirstCommand = new UnstarCommand(INDEX_FIRST_PERSON);
        UnstarCommand starSecondCommand = new UnstarCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(starFirstCommand.equals(starFirstCommand));

        // same values -> returns true
        UnstarCommand starFirstCommandCopy = new UnstarCommand(INDEX_FIRST_PERSON);
        assertTrue(starFirstCommand.equals(starFirstCommandCopy));

        // different types -> returns false
        assertFalse(starFirstCommand.equals(1));

        // null -> returns false
        assertFalse(starFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(starFirstCommand.equals(starSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnstarCommand unstarCommand = new UnstarCommand(targetIndex);
        String expected = UnstarCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unstarCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
