package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SINGLE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingMatchesKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.testutil.MeetingBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindMeetingCommand}.
 */
public class FindMeetingCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        List<String> descriptionKeywords1 = Collections.emptyList();
        List<String> descriptionKeywords2 = new ArrayList<>();
        descriptionKeywords2.add("hi");
        List<String> dateKeywords = Collections.emptyList();
        Set<Index> personIndices = Collections.emptySet();

        FindMeetingCommand findMeetingFirstCommand =
                new FindMeetingCommand(descriptionKeywords1, dateKeywords, personIndices);
        FindMeetingCommand findMeetingSecondCommand =
                new FindMeetingCommand(descriptionKeywords2, dateKeywords, personIndices);

        // same object -> returns true
        assertTrue(findMeetingFirstCommand.equals(findMeetingFirstCommand));

        // same values -> returns true
        FindMeetingCommand findMeetingFirstCommandCopy =
                new FindMeetingCommand(descriptionKeywords1, dateKeywords, personIndices);
        assertTrue(findMeetingFirstCommand.equals(findMeetingFirstCommandCopy));

        // different types -> returns false
        assertFalse(findMeetingFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findMeetingFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findMeetingFirstCommand.equals(findMeetingSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(Messages.MESSAGE_MEETINGS_LISTED_OVERVIEW, 0);

        List<String> descriptionKeywords = Collections.emptyList();
        List<String> dateKeywords = Collections.emptyList();
        Set<Index> personIndices = Collections.emptySet();
        Set<PersonId> idsToMatch = Collections.emptySet();
        MeetingMatchesKeywordsPredicate predicate =
                new MeetingMatchesKeywordsPredicate(descriptionKeywords, dateKeywords, idsToMatch);
        FindMeetingCommand command = new FindMeetingCommand(descriptionKeywords, dateKeywords, personIndices);
        expectedModel.updateFilteredMeetingList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredMeetingList());
    }

    @Test
    public void execute_oneKeyword_onePersonFound() {
        String expectedMessage = String.format(Messages.MESSAGE_MEETINGS_LISTED_OVERVIEW, 1);
        List<String> descriptionKeywords = new ArrayList<>();
        List<String> dateKeywords = Collections.emptyList();
        Set<Index> personIndices = Collections.emptySet();
        Set<PersonId> idsToMatch = Collections.emptySet();

        Set<Index> indices = VALID_INDEX_SINGLE;

        Person targetPerson = model.getFilteredPersonList().get(indices.iterator().next().getZeroBased());

        Set<String> participantIds = Set.of(targetPerson.getId().toString());

        Meeting meeting = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20260325)
                .withParticipants(participantIds)
                .build();

        descriptionKeywords.add(VALID_DESCRIPTION_PROJECT);

        model.addMeeting(meeting);

        MeetingMatchesKeywordsPredicate predicate =
                new MeetingMatchesKeywordsPredicate(descriptionKeywords, dateKeywords, idsToMatch);

        FindMeetingCommand command = new FindMeetingCommand(descriptionKeywords, dateKeywords, personIndices);

        expectedModel.updateFilteredMeetingList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void toStringMethod() {
        List<String> descriptionKeywords = new ArrayList<>();
        descriptionKeywords.add("hi");
        List<String> dateKeywords = Collections.emptyList();
        Set<Index> personIndices = Collections.emptySet();
        FindMeetingCommand findMeetingCommand =
                new FindMeetingCommand(descriptionKeywords, dateKeywords, personIndices);
        String expected = FindMeetingCommand.class.getCanonicalName()
                + "{descriptionKeywords=" + descriptionKeywords + ", "
                + "dateKeywords=" + dateKeywords + ", "
                + "personIndices=" + personIndices + "}";
        assertEquals(expected, findMeetingCommand.toString());
    }
}
