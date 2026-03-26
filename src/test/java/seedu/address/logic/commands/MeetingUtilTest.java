package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.logic.commands.MeetingUtil.collectParticipantIds;
import static seedu.address.logic.commands.MeetingUtil.createPersonWithGivenMeetings;
import static seedu.address.logic.commands.MeetingUtil.createPersonWithMeetingAdded;
import static seedu.address.logic.commands.MeetingUtil.removeMeetingFromAllParticipants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

public class MeetingUtilTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    private Person testPerson1 = model.getFilteredPersonList().get(0);
    private Person testPerson2 = model.getFilteredPersonList().get(1);

    private List<UUID> participantIds = List.of(testPerson1.getId(), testPerson2.getId());
    private Meeting testMeeting1 = new Meeting(VALID_DESCRIPTION_PROJECT,
            VALID_DATE_20260401, participantIds);
    private Meeting testMeeting2 = new Meeting(VALID_DESCRIPTION_TEAM,
            VALID_DATE_20260325, participantIds);

    @Test
    public void createPersonWithMeetingAdded_addsMeeting() throws CommandException {
        Person updated = createPersonWithMeetingAdded(testPerson1, testMeeting1);
        assertEquals(1, updated.getMeetings().size());
        assertTrue(updated.getMeetings().contains(testMeeting1));
    }

    @Test
    public void createPersonWithGivenMeetings_setsMeetings() {
        Set<Meeting> meetings = new HashSet<>();
        meetings.add(testMeeting1);
        meetings.add(testMeeting2);

        Person updated = createPersonWithGivenMeetings(testPerson1, meetings);
        assertEquals(2, updated.getMeetings().size());
        assertTrue(updated.getMeetings().contains(testMeeting1));
        assertTrue(updated.getMeetings().contains(testMeeting2));
    }

    @Test
    public void collectParticipantIds_returnsCorrectIds() throws CommandException {
        List<Person> persons = List.of(testPerson1, testPerson2);
        List<Index> indices = List.of(Index.fromZeroBased(0), Index.fromZeroBased(1));

        List<UUID> ids = collectParticipantIds(persons, indices);
        assertEquals(List.of(testPerson1.getId(), testPerson2.getId()), ids);
    }

    @Test
    public void collectParticipantIds_invalidIndex_throwsCommandException() {
        List<Person> persons = List.of(testPerson1);
        List<Index> indices = List.of(Index.fromZeroBased(1)); // out of bounds

        assertThrows(CommandException.class, () -> collectParticipantIds(persons, indices));
    }

    @Test
    public void removeMeetingFromAllParticipants_removesCorrectly() throws CommandException {
        // Add meeting to both persons in model
        model.setPerson(testPerson1, createPersonWithMeetingAdded(testPerson1, testMeeting1));
        model.setPerson(testPerson2, createPersonWithMeetingAdded(testPerson2, testMeeting1));

        // Call correct method signature
        removeMeetingFromAllParticipants(testMeeting1, model);

        // Verify
        Person updated1 = model.getFilteredPersonList().get(0);
        Person updated2 = model.getFilteredPersonList().get(1);

        assertEquals(0, updated1.getMeetings().size());
        assertEquals(0, updated2.getMeetings().size());
    }
}
