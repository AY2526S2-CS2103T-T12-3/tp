package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.FilterTagCommand.MESSAGE_FILTER_TAG_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;


public class FilterTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noTagsAdded_throwsCommandException() {
        Set<Tag> emptyTagSet = new HashSet<>();
        FilterTagCommand filterTagCommand = new FilterTagCommand(emptyTagSet);

        assertCommandFailure(filterTagCommand, model, FilterTagCommand.MESSAGE_NO_TAGS);
    }

    @Test
    public void execute_invalidTag_throwsCommandException() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("llllllllllllllll"));

        FilterTagCommand filterTagCommand = new FilterTagCommand(tags);

        assertCommandFailure(filterTagCommand, model, FilterTagCommand.MESSAGE_NO_VALID_TAG);
    }

    @Test
    public void execute_validIndexValidTags_success() {
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        Person personInList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tags = personInList.getTags();

        String tagList = tags.stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(", "));

        Predicate<Person> hasAnyTag = person -> {
            for (Tag tag : tags) {
                if (person.getTags().contains(tag)) {
                    return true;
                }
            }
            return false;
        };
        FilterTagCommand filterTagCommand = new FilterTagCommand(tags);

        expectedModel.updateFilteredPersonListStacked(hasAnyTag);

        String expectedMessage = String.format(MESSAGE_FILTER_TAG_SUCCESS,
                tagList,
                expectedModel.getFilteredPersonList().size());

        assertCommandSuccess(filterTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("friend"));
        Set<Tag> tags2 = new HashSet<>();
        tags1.add(new Tag("classmate"));
        FilterTagCommand filterTagFirstCommand = new FilterTagCommand(tags1);
        FilterTagCommand filterTagSecondCommand = new FilterTagCommand(tags2);

        // same object -> returns true
        assertTrue(filterTagFirstCommand.equals(filterTagFirstCommand));

        // same values -> returns true
        FilterTagCommand filterFirstCommandCopy = new FilterTagCommand(tags1);
        assertTrue(filterTagFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterTagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterTagFirstCommand.equals(null));

        // different tags -> returns false
        assertFalse(filterTagFirstCommand.equals(filterTagSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(targetIndex);
        Set<Tag> tags = new HashSet<>();
        FilterTagCommand filterTagCommand = new FilterTagCommand(tags);
        String expected = FilterTagCommand.class.getCanonicalName() + "{tags=" + tags + "}";
        assertEquals(expected, filterTagCommand.toString());
    }
}

