package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.PersonId;
import seedu.address.testutil.MeetingBuilder;

public class MeetingTest {
    private static final String VALID_ID_1 = "00000000-0000-0000-0000-000000000001";
    private static final String VALID_ID_2 = "00000000-0000-0000-0000-000000000002";

    public static final Set<String> VALID_IDS = Set.of(VALID_ID_1, VALID_ID_2);

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new MeetingBuilder().withDescription(null).withDate(VALID_DATE_20270325)
                        .withParticipants(VALID_IDS).build());
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new MeetingBuilder().withDescription(VALID_DESCRIPTION_PROJECT).withDate(null)
                        .withParticipants(VALID_IDS).build());
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new MeetingBuilder().withDescription(INVALID_DESCRIPTION)
                        .withDate(VALID_DATE_20270325).withParticipants(VALID_IDS).build());
    }

    @Test
    public void constructor_validMeeting_success() {
        Meeting meeting = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20270325)
                .withParticipants(VALID_IDS)
                .build();
        assertEquals(new Description(VALID_DESCRIPTION_PROJECT), meeting.getDescription());
        assertEquals(new MeetingDate(VALID_DATE_20270325), meeting.getDate());

        Set<String> actualIds = meeting.getParticipantsIDs().stream()
                .map(PersonId::toString)
                .collect(Collectors.toSet());

        assertEquals(VALID_IDS, actualIds);
    }

    @Test
    public void dateComparisons() {
        MeetingDate date1 = new MeetingDate(VALID_DATE_20270325);
        MeetingDate date2 = new MeetingDate(VALID_DATE_20270401);

        Meeting m1 = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20270325)
                .withParticipants(VALID_IDS)
                .build();

        Meeting m2 = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_TEAM)
                .withDate(VALID_DATE_20270401)
                .withParticipants(VALID_IDS)
                .build();

        assertTrue(m1.isBefore(date2.getDate()));
        assertTrue(m2.isAfter(date1.getDate()));

        assertFalse(m1.isAfter(date2.getDate()));
        assertFalse(m2.isBefore(date1.getDate()));
    }

    @Test
    public void toStringMethod() {
        Meeting meeting = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20270325)
                .withParticipants(VALID_IDS)
                .build();
        assertEquals(VALID_DESCRIPTION_PROJECT + " on " + VALID_DATE_20270325, meeting.toString());
    }

    @Test
    public void equals() {
        Meeting meeting1 = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20270325)
                .withParticipants(VALID_IDS)
                .build();
        Meeting meeting2 = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20270325)
                .withParticipants(VALID_IDS)
                .build();
        Meeting meetingDiffDescription = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_TEAM)
                .withDate(VALID_DATE_20270325)
                .withParticipants(VALID_IDS)
                .build();
        Meeting meetingDiffDate = new MeetingBuilder()
                .withDescription(VALID_DESCRIPTION_PROJECT)
                .withDate(VALID_DATE_20270401)
                .withParticipants(VALID_IDS)
                .build();

        // same object
        assertEquals(meeting1, meeting1);

        // same values
        assertEquals(meeting1, meeting2);

        // different description
        assertNotEquals(meeting1, meetingDiffDescription);

        // different date
        assertNotEquals(meeting1, meetingDiffDate);
    }
}
