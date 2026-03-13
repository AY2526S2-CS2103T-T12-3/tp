package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class AddTagCommandTest {
    private static final Index INDEX_FIRST_PERSON_COPY = Index.fromOneBased(1);

    private static final Tag NEW_TAG1 = new Tag("NewTag1");
    private static final Tag NEW_TAG1_COPY = new Tag("NewTag1");
    private static final Tag NEW_TAG2 = new Tag("NewTag2");
    private static final Tag NEW_TAG3 = new Tag("NewTag3");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noTagsAdded_failure() {
        Set<Tag> emptyTagSet = new HashSet<>();
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, emptyTagSet);

        assertCommandFailure(addTagCommand, model, AddTagCommand.MESSAGE_NO_TAGS);
    }

    @Test
    public void execute_addOneTagUnfilteredList_success() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(NEW_TAG1);

        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());
        Person editedPerson = addTagsToPerson(lastPerson, tagsToAdd);

        AddTagCommand addTagCommand = new AddTagCommand(indexLastPerson, tagsToAdd);

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS, tagsToAdd);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addOneTagFilteredList_success() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(NEW_TAG1);

        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = addTagsToPerson(personInFilteredList, tagsToAdd);

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS, tagsToAdd);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInFilteredList, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleTagsUnfilteredList_success() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(NEW_TAG1);
        tagsToAdd.add(NEW_TAG2);
        tagsToAdd.add(NEW_TAG3);

        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());
        Person editedPerson = addTagsToPerson(lastPerson, tagsToAdd);

        AddTagCommand addTagCommand = new AddTagCommand(indexLastPerson, tagsToAdd);

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS, tagsToAdd);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleTagsFilteredList_success() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(NEW_TAG1);
        tagsToAdd.add(NEW_TAG2);
        tagsToAdd.add(NEW_TAG3);

        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = addTagsToPerson(personInFilteredList, tagsToAdd);

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS, tagsToAdd);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInFilteredList, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleTagsToMultiplePersonsUnfilteredList_success() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(NEW_TAG1);
        tagsToAdd.add(NEW_TAG2);
        tagsToAdd.add(NEW_TAG3);

        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        targetIndices.add(INDEX_SECOND_PERSON);
        targetIndices.add(INDEX_THIRD_PERSON);

        AddTagCommand addTagCommand = new AddTagCommand(targetIndices, tagsToAdd);

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS, tagsToAdd);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        for (Index index : targetIndices) {
            Person personToEdit = expectedModel.getFilteredPersonList().get(index.getZeroBased());
            Person editedPerson = addTagsToPerson(personToEdit, tagsToAdd);
            System.out.println(editedPerson);
            expectedModel.setPerson(personToEdit, editedPerson);
        }

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(NEW_TAG1);
        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, tagSet);

        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Add tag to person in filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(NEW_TAG1);
        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, tagSet);

        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(NEW_TAG1);
        final AddTagCommand standardCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        // Dofferent tag instances but same values -> returns true
        Set<Tag> tagsToAddWithSameValues = new HashSet<>();
        tagsToAddWithSameValues.add(NEW_TAG1_COPY);
        AddTagCommand commandWithSameValues = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAddWithSameValues);
        assertEquals(standardCommand, commandWithSameValues);

        // Different index instances but same values -> returns true
        Set<Index> indicesWithSameValues = new HashSet<>();
        indicesWithSameValues.add(INDEX_FIRST_PERSON_COPY);
        AddTagCommand commandWithSetOfSameValue = new AddTagCommand(indicesWithSameValues, tagsToAddWithSameValues);
        assertEquals(standardCommand, commandWithSetOfSameValue);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different index -> returns false
        assertNotEquals(standardCommand, new AddTagCommand(INDEX_SECOND_PERSON, tagsToAdd));

        // different descriptor -> returns false
        Set<Tag> tagsToAddWithDifferentValues = new HashSet<>();
        tagsToAddWithDifferentValues.add(NEW_TAG2);
        assertNotEquals(standardCommand, new AddTagCommand(INDEX_FIRST_PERSON, tagsToAddWithDifferentValues));
    }

    @Test
    public void toStringMethod() {
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(Index.fromOneBased(1));
        Set<Tag> tags = new HashSet<>();
        tags.add(NEW_TAG1);
        AddTagCommand addTagCommand = new AddTagCommand(targetIndices, tags);

        String expected = AddTagCommand.class.getCanonicalName() + "{targetIndices=" + targetIndices + ", tags="
                + tags + "}";
        assertEquals(expected, addTagCommand.toString());
    }

    private Person addTagsToPerson(Person personToEdit, Collection<Tag> tags) {
        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        tags.forEach(tag -> updatedTags.add(tag));

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                updatedTags
        );
    }
}
