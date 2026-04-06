package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.StarCommand.MESSAGE_NO_VALID_PERSONS_STAR;
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
 * {@code StarCommand}.
 */
public class StarCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_starValidIndexUnfilteredList_success() {
        Person personToStar = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));
        StarCommand starCommand = new StarCommand(targetIndices);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> newTags = new HashSet<>(personToStar.getTags());
        newTags.add(new Tag(Tag.STAR_TAG));

        Person starredPerson = amendTagsOfPerson(personToStar, newTags);

        expectedModel.setPerson(personToStar, starredPerson);

        String expectedMessage = String.format(StarCommand.MESSAGE_STAR_PERSON_SUCCESS,
                starredPerson.getName().fullName);

        assertCommandSuccess(starCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));
        targetIndices.add(outOfBoundIndex);
        StarCommand starCommand = new StarCommand(targetIndices);

        assertCommandFailure(starCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_starValidIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToStar = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));
        StarCommand starCommand = new StarCommand(targetIndices);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> newTags = new HashSet<>(personToStar.getTags());
        newTags.add(new Tag(Tag.STAR_TAG));

        Person starredPerson = amendTagsOfPerson(personToStar, newTags);

        expectedModel.setPerson(personToStar, starredPerson);

        String expectedMessage = String.format(StarCommand.MESSAGE_STAR_PERSON_SUCCESS,
                starredPerson.getName().fullName);

        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(starCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_starPersonWithStarTag_failure() {
        Person personToStar = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));

        StarCommand starCommand = new StarCommand(targetIndices);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> newTags = new HashSet<>(personToStar.getTags());
        newTags.add(new Tag(Tag.STAR_TAG));
        Person starredPerson = amendTagsOfPerson(personToStar, newTags);

        model.setPerson(personToStar, starredPerson);
        expectedModel.setPerson(personToStar, starredPerson);

        String expectedMessage = String.format(MESSAGE_NO_VALID_PERSONS_STAR);

        assertCommandFailure(starCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(outOfBoundIndex);

        StarCommand starCommand = new StarCommand(targetIndices);

        assertCommandFailure(starCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Index> targetIndices1 = new HashSet<>();
        targetIndices1.add(INDEX_FIRST_PERSON);
        Set<Index> targetIndices2 = new HashSet<>();
        StarCommand starFirstCommand = new StarCommand(targetIndices1);
        StarCommand starSecondCommand = new StarCommand(targetIndices2);

        // same object -> returns true
        assertTrue(starFirstCommand.equals(starFirstCommand));

        // same values -> returns true
        StarCommand starFirstCommandCopy = new StarCommand(targetIndices1);
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
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));
        StarCommand starCommand = new StarCommand(targetIndices);
        String expected = StarCommand.class.getCanonicalName() + "{targetIndices=" + targetIndices + "}";
        assertEquals(expected, starCommand.toString());
    }

}
