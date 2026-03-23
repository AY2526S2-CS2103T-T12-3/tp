package seedu.address.storage;

import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DATE_NON_EXISTENT;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DATE_WRONG_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.storage.JsonAdaptedPersonTest.VALID_MEETINGS;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.meeting.Meeting;

public class JsonAdaptedMeetingTest {
    public static final String VALID_ID_1 = "00000000-0000-0000-0000-000000000001";
    public static final String VALID_ID_2 = "00000000-0000-0000-0000-000000000002";

    public static final String INVALID_ID_1 = "bruh";
    public static final String INVALID_ID_2 = "";

    public static final List<String> VALID_IDS = List.of(VALID_ID_1, VALID_ID_2);
    public static final List<String> INVALID_IDS = List.of(INVALID_ID_1, INVALID_ID_2);

    @Test
    public void toModelType_validMeetings_success() throws Exception {
        for (JsonAdaptedMeeting meeting : VALID_MEETINGS) {
            Meeting modelMeeting = meeting.toModelType();
            assert(modelMeeting.getDescription().equals(meeting.getDescription()));
            assert(modelMeeting.getDate().toString().equals(meeting.getDate()));
            assert(modelMeeting.getParticipantsID().size() == VALID_IDS.size()); // optional extra check
        }
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(null,
                VALID_DATE_20260325.toString(), VALID_IDS);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    public void toModelType_emptyDescription_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(INVALID_DESCRIPTION,
                VALID_DATE_20260325.toString(), VALID_IDS);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT,
                null, VALID_IDS);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    public void toModelType_emptyDate_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT,
                INVALID_DESCRIPTION, VALID_IDS);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    public void toModelType_nonExistentDate_throwsException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT,
                INVALID_DATE_NON_EXISTENT, VALID_IDS);
        assertThrows(Exception.class, meeting::toModelType);
    }

    @Test
    public void toModelType_invalidIDs_throwsException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT,
                VALID_DATE_20260325.toString(), INVALID_IDS);
        assertThrows(Exception.class, meeting::toModelType);
    }

    @Test
    public void toModelType_wrongFormat_throwsException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT,
                INVALID_DATE_WRONG_FORMAT, VALID_IDS);
        assertThrows(Exception.class, meeting::toModelType);
    }
}
