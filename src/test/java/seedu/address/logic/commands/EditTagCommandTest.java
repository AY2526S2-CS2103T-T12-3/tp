package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.index.Index.fromOneBased;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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

public class EditTagCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_oldTagInvalid_failure() {
        Tag oldTag = new Tag("lllllllllllllllllll");
        Tag newTag = new Tag("test");
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        EditTagCommand editTagCommand = new EditTagCommand(targetIndices, oldTag, newTag);

        assertCommandFailure(editTagCommand, model, EditTagCommand.MESSAGE_OLDTAG_INVALID);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        List<Index> targetIndices = new ArrayList<>();
        Index outOfBoundIndex = fromOneBased(model.getFilteredPersonList().size() + 1);
        targetIndices.add(outOfBoundIndex);
        Tag oldTag = new Tag("hi");
        Tag newTag = new Tag("bye");

        EditTagCommand editTagCommand = new EditTagCommand(targetIndices, oldTag, newTag);

        assertCommandFailure(editTagCommand, model, "Error: " + Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validParamsGlobal_success() {
        List<Index> targetIndices = new ArrayList<>();
        List<Person> lastShownList = model.getFilteredPersonList();
        for (int i = 0; i < lastShownList.size(); i++) {
            targetIndices.add(fromOneBased(i + 1));
        }
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> initialTags = new HashSet<>(personToEdit.getTags());
        Set<Tag> finalTags = new HashSet<>(personToEdit.getTags());
        Tag oldTag = new Tag("hi");
        Tag newTag = new Tag("bye");
        initialTags.add(oldTag);
        finalTags.add(newTag);
        Person personWithOldTag = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                initialTags,
                personToEdit.getMeetings()

        );
        EditTagCommand editTagCommand = new EditTagCommand(targetIndices, oldTag, newTag);
        model.setPerson(personToEdit, personWithOldTag);

        Person personWithNewTag = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                finalTags,
                personToEdit.getMeetings()
        );

        String expectedMessage = String.format(
                EditTagCommand.MESSAGE_EDIT_TAG_SUCCESS_GLOBAL, oldTag, newTag);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithOldTag, personWithNewTag);

        assertCommandSuccess(editTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validParamsIndices_success() {
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(fromOneBased(1));
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> initialTags = new HashSet<>(personToEdit.getTags());
        Set<Tag> finalTags = new HashSet<>(personToEdit.getTags());
        Tag oldTag = new Tag("hi");
        Tag newTag = new Tag("bye");
        initialTags.add(oldTag);
        finalTags.add(newTag);
        Person personWithOldTag = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                initialTags,
                personToEdit.getMeetings()
        );

        EditTagCommand editTagCommand = new EditTagCommand(targetIndices, oldTag, newTag);
        model.setPerson(personToEdit, personWithOldTag);

        Person personWithNewTag = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                finalTags,
                personToEdit.getMeetings()
        );

        String expectedMessage = String.format(
                EditTagCommand.MESSAGE_EDIT_TAG_SUCCESS_INDICES, oldTag, newTag);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithOldTag, personWithNewTag);

        assertCommandSuccess(editTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        List<Index> targetIndices1 = new ArrayList<>();
        List<Index> targetIndices2 = new ArrayList<>();
        Tag oldTag = new Tag("hi");
        Tag newTag = new Tag("by");
        targetIndices1.add(INDEX_FIRST_PERSON);
        targetIndices2.add(INDEX_SECOND_PERSON);
        EditTagCommand editTagFirstCommand = new EditTagCommand(targetIndices1, oldTag, newTag);
        EditTagCommand editTagSecondCommand = new EditTagCommand(targetIndices2, oldTag, newTag);

        // same object -> returns true
        assertTrue(editTagFirstCommand.equals(editTagFirstCommand));

        // same values -> returns true
        EditTagCommand editFirstCommandCopy = new EditTagCommand(targetIndices1, oldTag, newTag);
        assertTrue(editTagFirstCommand.equals(editFirstCommandCopy));

        // different types -> returns false
        assertFalse(editTagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(editTagFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(editTagFirstCommand.equals(editTagSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = fromOneBased(1);
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(targetIndex);
        Tag oldTag = new Tag("hi");
        Tag newTag = new Tag("bye");
        EditTagCommand editTagCommand = new EditTagCommand(targetIndices, oldTag, newTag);
        String expected = EditTagCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, editTagCommand.toString());
    }
}
