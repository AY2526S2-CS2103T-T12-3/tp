package seedu.address.storage;

import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DATE_NON_EXISTENT;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DATE_WRONG_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.meeting.Meeting;

class JsonAdaptedMeetingTest {
    @Test
    void toModelType_validMeetings_success() throws Exception {
        List<JsonAdaptedMeeting> validMeetings = List.of(
                new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325.toString()),
                new JsonAdaptedMeeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20260401.toString())
        );

        for (JsonAdaptedMeeting meeting : validMeetings) {
            Meeting modelMeeting = meeting.toModelType();
            assert(modelMeeting.getDescription().equals(meeting.getDescription()));
            assert(modelMeeting.getDate().toString().equals(meeting.getDate()));
        }
    }

    @Test
    void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(null, VALID_DATE_20260325.toString());
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    void toModelType_emptyDescription_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(INVALID_DESCRIPTION, VALID_DATE_20260325.toString());
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT, null);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    void toModelType_emptyDate_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT, INVALID_DESCRIPTION);
        assertThrows(IllegalValueException.class, meeting::toModelType);
    }

    @Test
    void toModelType_nonExistentDate_throwsException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT, INVALID_DATE_NON_EXISTENT);
        assertThrows(Exception.class, meeting::toModelType);
    }

    @Test
    void toModelType_wrongFormat_throwsException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT, INVALID_DATE_WRONG_FORMAT);
        assertThrows(Exception.class, meeting::toModelType);
    }
}
