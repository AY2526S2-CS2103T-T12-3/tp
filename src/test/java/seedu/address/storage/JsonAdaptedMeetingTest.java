package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DATE_WRONG_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.storage.JsonAdaptedPersonTest.VALID_MEETINGS;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.meeting.Meeting;

public class JsonAdaptedMeetingTest {
    public static final String VALID_ID_1 = "00000000-0000-0000-0000-000000000001";
    public static final String VALID_ID_2 = "00000000-0000-0000-0000-000000000002";

    public static final String INVALID_ID_1 = "bruh";
    public static final String INVALID_ID_2 = "";

    public static final Set<String> VALID_IDS = Set.of(VALID_ID_1, VALID_ID_2);
    public static final Set<String> INVALID_IDS = Set.of(INVALID_ID_1, INVALID_ID_2);
    public static final Set<String> PARTIALLY_INVALID_IDS = Set.of(INVALID_ID_1, VALID_ID_1);

    @Test
    public void toModelType_validMeetings_success() throws Exception {
        for (JsonAdaptedMeeting meeting : VALID_MEETINGS) {
            Meeting modelMeeting = meeting.toModelType();
            assert(modelMeeting.getDescription().description.equals(meeting.getDescription()));
            assert(modelMeeting.getDate().toString().equals(meeting.getDate()));
            assert(modelMeeting.getParticipantsIDs().size() == VALID_IDS.size()); // optional extra check
        }
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(null,
                VALID_DATE_20270325.toString(), VALID_IDS);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT,
                null, VALID_IDS);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    public void toModelType_invalidIDs_ignored() throws Exception {
        JsonAdaptedMeeting meetingWithInvalidIds = new JsonAdaptedMeeting(
                VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20270325,
                INVALID_IDS
        );

        Meeting modelMeeting = meetingWithInvalidIds.toModelType();

        // Check that invalid IDs were ignored and meeting still has 0 participants
        assertTrue(modelMeeting.getParticipantsIDs().isEmpty());
    }

    @Test
    public void toModelType_partiallyInvalidIDs_ignored() throws Exception {
        JsonAdaptedMeeting meetingWithInvalidIds = new JsonAdaptedMeeting(
                VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20270325,
                PARTIALLY_INVALID_IDS
        );

        Meeting modelMeeting = meetingWithInvalidIds.toModelType();

        // Check that invalid IDs were ignored but valid ones were accepted.
        assertEquals(1, modelMeeting.getParticipantsIDs().size());
        assertEquals(VALID_ID_1, modelMeeting.getParticipantsIDs().iterator().next().toString());
    }

    @Test
    public void toModelType_wrongFormat_throwsException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT,
                INVALID_DATE_WRONG_FORMAT, VALID_IDS);
        assertThrows(Exception.class, meeting::toModelType);
    }
}
