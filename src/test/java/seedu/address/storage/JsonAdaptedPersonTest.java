package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

class JsonAdaptedPersonTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_ID = "not-a-uuid";

    private static final List<JsonAdaptedTag> VALID_TAGS_AMY = List.of(
            new JsonAdaptedTag(VALID_TAG_HUSBAND), new JsonAdaptedTag(VALID_TAG_FRIEND)
    );

    // Dummy meetings for testing
    private static final List<JsonAdaptedMeeting> VALID_MEETINGS = List.of(
            new JsonAdaptedMeeting("Project Meeting", "2026-03-25"),
            new JsonAdaptedMeeting("Team Meeting", "2026-04-01")
    );

    private static final List<JsonAdaptedMeeting> INVALID_MEETINGS = List.of(
            new JsonAdaptedMeeting("", "2026-03-25"),
            new JsonAdaptedMeeting("Project Meeting", "")
    );

    // Valid cases
    @Test
    void toModelType_validPersonWithId_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        assertEquals(UUID.fromString(VALID_ID_AMY), person.toModelType().getId());
    }

    @Test
    void toModelType_validPersonWithoutId_generatesNewId() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                null, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        assertEquals(VALID_NAME_AMY, person.toModelType().getName().fullName);
        UUID id = person.toModelType().getId();
        assertEquals(true, id != null);
    }

    @Test
    void toModelType_validPhoneOnly_noExceptionThrown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, null, VALID_TAGS_AMY, VALID_MEETINGS);
        person.toModelType();
    }

    @Test
    void toModelType_validEmailOnly_noExceptionThrown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, null, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        person.toModelType();
    }

    @Test
    void toModelType_validPhoneAndEmail_noExceptionThrown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        person.toModelType();
    }

    // Invalid cases

    @Test
    void toModelType_invalidId_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                INVALID_ID, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        assertThrows(IllegalValueException.class, JsonAdaptedPerson.INVALID_UUID_MESSAGE, person::toModelType);
    }

    @Test
    void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, INVALID_NAME, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, null, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        String expectedMessage = String.format(JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, INVALID_PHONE, VALID_EMAIL_AMY, VALID_TAGS_AMY, VALID_MEETINGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, INVALID_EMAIL, VALID_TAGS_AMY, VALID_MEETINGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    void toModelType_nullPhoneAndEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, null, null, VALID_TAGS_AMY, VALID_MEETINGS);
        String expectedMessage = String.format(JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Phone.class.getSimpleName() + " or " + Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS_AMY);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, invalidTags, VALID_MEETINGS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    void toModelType_invalidMeetings_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY, INVALID_MEETINGS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }
}
