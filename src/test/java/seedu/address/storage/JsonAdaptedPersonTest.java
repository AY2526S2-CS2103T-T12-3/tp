package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.storage.JsonAdaptedMeetingTest.INVALID_ID_1;
import static seedu.address.storage.JsonAdaptedMeetingTest.VALID_ID_1;
import static seedu.address.storage.JsonAdaptedMeetingTest.VALID_ID_2;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.Phone;

class JsonAdaptedPersonTest {

    private static final String INVALID_NAME = "!Rachel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "/friend";

    private static final List<JsonAdaptedTag> VALID_TAGS_AMY = List.of(
            new JsonAdaptedTag(VALID_TAG_HUSBAND), new JsonAdaptedTag(VALID_TAG_FRIEND)
    );

    // Checkstyle does not agree with reusing Lists, new ID lists remade here
    private static final Set<String> VALID_IDS = Set.of(VALID_ID_1, VALID_ID_2);

    // Dummy meetings for testing
    public static final List<JsonAdaptedMeeting> VALID_MEETINGS = List.of(
            new JsonAdaptedMeeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20270325.toString(), VALID_IDS),
            new JsonAdaptedMeeting(VALID_DESCRIPTION_TEAM, VALID_DATE_20270401.toString(), VALID_IDS)
    );

    // Valid cases
    @Test
    public void toModelType_validPersonWithId_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        assertEquals(new PersonId(VALID_ID_AMY), person.toModelType().getId());
    }

    @Test
    public void toModelType_validPersonWithoutId_generatesNewId() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null,
                VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        assertEquals(VALID_NAME_AMY, person.toModelType().getName().fullName);
        PersonId id = person.toModelType().getId();
        assertEquals(true, id != null);
    }

    @Test
    public void toModelType_validPhoneOnly_noExceptionThrown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, null, VALID_TAGS_AMY);
        person.toModelType();
    }

    @Test
    public void toModelType_validEmailOnly_noExceptionThrown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, null, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        person.toModelType();
    }

    @Test
    public void toModelType_validPhoneAndEmail_noExceptionThrown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        person.toModelType();
    }

    @Test
    public void toModelType_invalidId_noExceptionThrown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                INVALID_ID_1, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        person.toModelType();
    }

    // Invalid cases

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, INVALID_NAME, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, null, VALID_PHONE_AMY, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        String expectedMessage = String.format(JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, INVALID_PHONE, VALID_EMAIL_AMY, VALID_TAGS_AMY);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, INVALID_EMAIL, VALID_TAGS_AMY);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhoneAndEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, null, null, VALID_TAGS_AMY);
        String expectedMessage = String.format(JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Phone.class.getSimpleName() + " or " + Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS_AMY);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID_AMY, VALID_NAME_AMY, VALID_PHONE_AMY, VALID_EMAIL_AMY, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }
}
